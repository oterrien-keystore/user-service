package com.ote.user.spring;

import com.ote.common.persistence.model.*;
import com.ote.common.persistence.repository.*;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import com.ote.user.rights.api.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("Test")
@Configuration
@Slf4j
public class DataConfigurationMock {

    @Autowired
    private IEncryptorService encryptorService;

    @Autowired
    private IUserJpaRepository userRepository;

    @Autowired
    private IApplicationJpaRepository applicationRepository;

    @Autowired
    private IPerimeterJpaRepository perimeterRepository;

    @Autowired
    private IPrivilegeJpaRepository privilegeRepository;

    @Autowired
    private IUserRightJpaRepository userRightRepository;

    @Autowired
    private IUserRightDetailJpaRepository userRightDetailRepository;

    @Autowired
    private ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private ISecurityGroupRightJpaRepository securityGroupRightRepository;

    @Autowired
    private ISecurityGroupRightDetailJpaRepository securityGroupRightDetailRepository;

    public void init() {

        // Create Users
        createUsers(
                new User("user1", "password"),
                new User("user2", "password"),
                new User("user3", "password"));

        // Create Applications
        createApplications("TEST_SERVICE", "USER_SERVICE");

        // Create Perimeters
        createPerimeters("PARENT", "PARENT/CHILD");

        // Create Privileges
        createPrivileges(
                new Privilege("ADMIN"),
                new Privilege("WRITE", "ADMIN"),
                new Privilege("READ", "WRITE"),
                new Privilege("OTHER"));

        //Create UserRights
        createUserRights(
                new UserRight("user1", "USER_SERVICE", "PARENT", "READ"),
                new UserRight("user1", "TEST_SERVICE", "PARENT", "READ"),
                new UserRight("user2", "TEST_SERVICE", "PARENT/CHILD", "READ"),
                new UserRight("user3", "TEST_SERVICE", "PARENT/CHILD", "READ")
        );

        // Create Security Group
        createSecurityGroup(new SecurityGroup("TEST_SERVICE_ADMINS", "user1"));
        createSecurityGroup(new SecurityGroup("TEST_SERVICE_READERS", "user2"));

        // Create Security Group Rights
        createSecurityGroupRight(new SecurityGroupRight("TEST_SERVICE_ADMINS", "TEST_SERVICE", "PARENT", "ADMIN"));
        createSecurityGroupRight(new SecurityGroupRight("TEST_SERVICE_ADMINS", "TEST_SERVICE", "PARENT/CHILD", "WRITE"));
        createSecurityGroupRight(new SecurityGroupRight("TEST_SERVICE_READERS", "TEST_SERVICE", "PARENT", "READ"));
    }

    public void clean() {
        userRightDetailRepository.deleteAll();
        userRightRepository.deleteAll();
        securityGroupRightDetailRepository.deleteAll();
        securityGroupRightRepository.deleteAll();
        securityGroupRepository.deleteAll();
        userRepository.deleteAll();
        applicationRepository.deleteAll();
        perimeterRepository.deleteAll();
        privilegeRepository.deleteAll();
    }

    private void createUsers(User... users) {
        Stream.of(users).
                forEach(p -> {
                    try {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setLogin(p.login);
                        userEntity.setPassword(encryptorService.encrypt(p.password));
                        if (!userRepository.existsByLogin(p.login)) {
                            userRepository.save(userEntity);
                            userRepository.flush();
                        }
                    } catch (EncryptingException e) {
                        log.info(e.getMessage(), e);
                    }
                });
    }

    private void createApplications(String... applications) {
        Stream.of(applications).
                forEach(p -> {
                    ApplicationEntity applicationEntity = new ApplicationEntity();
                    applicationEntity.setCode(p);
                    if (!applicationRepository.existsByCode(p)) {
                        applicationRepository.save(applicationEntity);
                        applicationRepository.flush();
                    }
                });
    }

    private void createPerimeters(String... perimeters) {
        Stream.of(perimeters).
                forEach(p -> {
                    Path perimeterPath = new Path.Parser(p).get();
                    AtomicReference<String> previous = new AtomicReference<>();
                    perimeterPath.forEach(pp -> {
                        PerimeterEntity perimeterEntity = new PerimeterEntity();
                        perimeterEntity.setCode(pp);
                        String parent = previous.get();
                        if (parent != null) {
                            Assumptions.assumeTrue(perimeterRepository.existsByCode(parent));
                            perimeterEntity.setParent(perimeterRepository.findByCode(parent));
                        }
                        if (!perimeterRepository.existsByCode(pp)) {
                            perimeterRepository.save(perimeterEntity);
                            perimeterRepository.flush();
                        }
                        previous.set(pp);
                    });
                });
    }

    private void createPrivileges(Privilege... privileges) {
        Stream.of(privileges).
                forEach(p -> {
                    PrivilegeEntity privilegeEntity = new PrivilegeEntity();
                    privilegeEntity.setCode(p.code);
                    if (p.parent != null) {
                        Assumptions.assumeTrue(privilegeRepository.existsByCode(p.parent));
                        privilegeEntity.setParent(privilegeRepository.findByCode(p.parent));
                    }
                    if (!privilegeRepository.existsByCode(p.code)) {
                        privilegeRepository.save(privilegeEntity);
                        privilegeRepository.flush();
                    }
                });
    }

    private void createUserRights(UserRight... userRights) {
        Stream.of(userRights).
                peek(p -> {
                    String messageTemplate = "%s %s should exist";
                    Assumptions.assumeTrue(userRepository.existsByLogin(p.user), String.format(messageTemplate, "User", p.user));
                    Assumptions.assumeTrue(applicationRepository.existsByCode(p.application), String.format(messageTemplate, "Application", p.application));
                    Assumptions.assumeTrue(perimeterRepository.existsByCode(p.perimeter), String.format(messageTemplate, "Perimeter", p.perimeter));
                    p.privileges.forEach(pr -> Assumptions.assumeTrue(privilegeRepository.existsByCode(pr), String.format(messageTemplate, "Privilege", pr)));
                }).
                forEach(p -> {
                    if (!userRightRepository.existsByUserLoginAndApplicationCode(p.user, p.application)) {
                        UserRightEntity entity = new UserRightEntity();
                        entity.setUser(userRepository.findByLogin(p.user));
                        entity.setApplication(applicationRepository.findByCode(p.application));
                        userRightRepository.save(entity);
                        userRightRepository.flush();
                    }
                    UserRightDetailEntity userRightDetailEntity;
                    if (userRightDetailRepository.existsByUserLoginAndApplicationCodeAndPerimeterCode(p.user, p.application, p.perimeter)) {
                        userRightDetailEntity = userRightDetailRepository.findByUserLoginAndApplicationCodeAndPerimeterCode(p.user, p.application, p.perimeter);
                    } else {
                        userRightDetailEntity = new UserRightDetailEntity();
                        userRightDetailEntity.setUserRight(userRightRepository.findByUserLoginAndApplicationCode(p.user, p.application));
                        userRightDetailEntity.setPerimeter(perimeterRepository.findByCode(p.perimeter));
                        userRightDetailEntity.setPrivileges(new HashSet<>());
                    }
                    p.privileges.stream().
                            map(privilegeRepository::findByCode).
                            forEach(pr -> userRightDetailEntity.getPrivileges().add(pr));
                    userRightDetailRepository.save(userRightDetailEntity);
                    userRightDetailRepository.flush();
                });
    }

    private void createSecurityGroup(SecurityGroup... securityGroups) {
        Stream.of(securityGroups).
                peek(p -> p.users.forEach(u -> Assumptions.assumeTrue(userRepository.existsByLogin(u)))).
                forEach(p -> {
                    SecurityGroupEntity securityGroupEntity = new SecurityGroupEntity();
                    securityGroupEntity.setCode(p.code);
                    securityGroupEntity.setUsers(p.users.stream().map(u -> userRepository.findByLogin(u)).collect(Collectors.toSet()));
                    if (!securityGroupRepository.existsByCode(p.code)) {
                        securityGroupRepository.save(securityGroupEntity);
                        securityGroupRepository.flush();
                    }
                });
    }

    private void createSecurityGroupRight(SecurityGroupRight... securityGroupRights) {
        Stream.of(securityGroupRights).
                peek(p -> {
                    String messageTemplate = "%s %s should exist";
                    Assumptions.assumeTrue(securityGroupRepository.existsByCode(p.code), String.format(messageTemplate, "SecurityGroup", p.code));
                    Assumptions.assumeTrue(applicationRepository.existsByCode(p.application), String.format(messageTemplate, "Application", p.application));
                    Assumptions.assumeTrue(perimeterRepository.existsByCode(p.perimeter), String.format(messageTemplate, "Perimeter", p.perimeter));
                    p.privileges.forEach(pr -> Assumptions.assumeTrue(privilegeRepository.existsByCode(pr), String.format(messageTemplate, "Privilege", pr)));
                }).
                forEach(p -> {
                    SecurityGroupRightEntity securityGroupRightEntity;
                    if (securityGroupRightRepository.existsBySecurityGroupCodeAndApplicationCode(p.code, p.application)) {
                        securityGroupRightEntity = securityGroupRightRepository.findBySecurityGroupCodeAndApplicationCode(p.code, p.application);
                    } else {
                        securityGroupRightEntity = new SecurityGroupRightEntity();
                        securityGroupRightEntity.setSecurityGroup(securityGroupRepository.findByCode(p.code));
                        securityGroupRightEntity.setApplication(applicationRepository.findByCode(p.application));
                        securityGroupRightEntity = securityGroupRightRepository.save(securityGroupRightEntity);
                    }

                    SecurityGroupRightDetailEntity securityGroupRightDetailEntity;
                    if (securityGroupRightDetailRepository.existsBySecurityGroupCodeAndApplicationCodeAndPerimeterCode(p.code, p.application, p.perimeter)) {
                        securityGroupRightDetailEntity = securityGroupRightDetailRepository.findBySecurityGroupCodeAndApplicationCodeAndPerimeterCode(p.code, p.application, p.perimeter);
                    } else {
                        securityGroupRightDetailEntity = new SecurityGroupRightDetailEntity();
                        securityGroupRightDetailEntity.setSecurityGroupRight(securityGroupRightEntity);
                        securityGroupRightDetailEntity.setPerimeter(perimeterRepository.findByCode(p.perimeter));
                        securityGroupRightDetailEntity.setPrivileges(new HashSet<>());
                    }
                    p.privileges.stream().
                            map(privilegeRepository::findByCode).
                            forEach(pr -> securityGroupRightDetailEntity.getPrivileges().add(pr));
                    securityGroupRightDetailRepository.save(securityGroupRightDetailEntity);
                    securityGroupRightDetailRepository.flush();
                });
    }

    @RequiredArgsConstructor
    private class Privilege {
        private final String code;
        private final String parent;

        Privilege(String code) {
            this(code, null);
        }
    }

    @RequiredArgsConstructor
    private class User {
        private final String login;
        private final String password;
    }

    private class UserRight {
        private final String user;
        private final String application;
        private final String perimeter;
        private final List<String> privileges;

        UserRight(String user, String application, String perimeter, String... privileges) {
            this.user = user;
            this.application = application;
            this.perimeter = perimeter;
            this.privileges = Arrays.asList(privileges);
        }
    }

    private class SecurityGroup {
        private final String code;
        private final List<String> users;

        SecurityGroup(String code, String... users) {
            this.code = code;
            this.users = Arrays.asList(users);
        }
    }

    private class SecurityGroupRight {
        private final String code;
        private final String application;
        private final String perimeter;
        private final List<String> privileges;

        SecurityGroupRight(String code, String application, String perimeter, String... privileges) {
            this.code = code;
            this.application = application;
            this.perimeter = perimeter;
            this.privileges = Arrays.asList(privileges);
        }
    }

}