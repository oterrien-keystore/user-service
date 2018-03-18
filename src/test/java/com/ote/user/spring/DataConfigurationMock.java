package com.ote.user.spring;

import com.ote.common.persistence.model.*;
import com.ote.common.persistence.repository.*;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import com.ote.user.rights.api.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
    private ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private ISecurityGroupRightJpaRepository securityGroupRightRepository;

    @Autowired
    private ISecurityGroupRightDetailJpaRepository securityGroupRightDetailRepository;

    public DataConfigurationMock() {
        log.warn("###### MOCK ##### ");
    }

    public void init() {
        log.warn("###### MOCK ##### Init database with samples");

        // Create Users
        createUsers(
                new User("user1", "password"),
                new User("user2", "password"),
                new User("user3", "password"));

        // Create Applications
        createApplications("App1", "App2", "User-Service TEST");

        // Create Perimeters
        createPerimeters("PARENT", "PARENT/CHILD", "SECURITY_GROUP", "USER");

        // Create Privileges
        createPrivileges(
                new Privilege("ADMIN"),
                new Privilege("WRITE", "ADMIN"),
                new Privilege("READ", "WRITE"));

        // Create Security Group
        createSecurityGroup("Admins", "Contributors", "Readers");

        addUsersToSecurityGroup("Admins", "user1");
        addUsersToSecurityGroup("Contributors", "user2");
        addUsersToSecurityGroup("Readers", "user3");

        // Create Security Group Rights
        createSecurityGroupRight(
                new SecurityGroupRight("Admins", "App1", "PARENT", "ADMIN"),
                new SecurityGroupRight("Admins", "App2", "PARENT", "ADMIN"),
                new SecurityGroupRight("Admins", "User-Service TEST", "SECURITY_GROUP", "ADMIN"),
                new SecurityGroupRight("Admins", "User-Service TEST", "USER", "ADMIN"),
                new SecurityGroupRight("Contributors", "App1", "PARENT", "WRITE"),
                new SecurityGroupRight("Contributors", "App2", "PARENT/CHILD", "WRITE"),
                new SecurityGroupRight("Readers", "App1", "PARENT", "READ"));
    }

    public void clean() {
        log.warn("###### MOCK ##### Clean database");
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
                            assert perimeterRepository.existsByCode(parent) : "Perimeter " + parent + " should exist";
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
                        assert privilegeRepository.existsByCode(p.parent) : "Privilege " + p.parent + " should exist";
                        privilegeEntity.setParent(privilegeRepository.findByCode(p.parent));
                    }
                    if (!privilegeRepository.existsByCode(p.code)) {
                        privilegeRepository.save(privilegeEntity);
                        privilegeRepository.flush();
                    }
                });
    }

    private void createSecurityGroup(String... securityGroups) {
        Stream.of(securityGroups).
                forEach(p -> {
                    SecurityGroupEntity securityGroupEntity = new SecurityGroupEntity();
                    securityGroupEntity.setCode(p);
                    securityGroupEntity.setUsers(new HashSet<>());
                    if (!securityGroupRepository.existsByCode(p)) {
                        securityGroupRepository.save(securityGroupEntity);
                        securityGroupRepository.flush();
                    }
                });
    }

    private void addUsersToSecurityGroup(String securityGroup, String... users) {

        assert securityGroupRepository.existsByCode(securityGroup) : "SecurityGroup " + securityGroup + " should exist";

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(securityGroup);

        Stream.of(users).
                peek(u -> {
                    assert userRepository.existsByLogin(u) : "User " + u + " should exist";
                }).
                forEach(u -> {
                    securityGroupEntity.getUsers().add(userRepository.findByLogin(u));
                    securityGroupRepository.save(securityGroupEntity);
                    securityGroupRepository.flush();
                });
    }


    private void createSecurityGroupRight(SecurityGroupRight... securityGroupRights) {
        Stream.of(securityGroupRights).
                peek(p -> {
                    String messageTemplate = "%s %s should exist";
                    assert securityGroupRepository.existsByCode(p.code) : String.format(messageTemplate, "SecurityGroup", p.code);
                    assert applicationRepository.existsByCode(p.application) : String.format(messageTemplate, "Application", p.application);
                    assert perimeterRepository.existsByCode(p.perimeter) : String.format(messageTemplate, "Perimeter", p.perimeter);
                    p.privileges.forEach(pr -> {
                        assert privilegeRepository.existsByCode(pr) : String.format(messageTemplate, "Privilege", pr);
                    });
                }).
                forEach(p -> {
                    SecurityGroupRightEntity securityGroupRightEntity;
                    if (securityGroupRightRepository.existsBySecurityGroupCodeAndApplicationCode(p.code, p.application)) {
                        securityGroupRightEntity = securityGroupRightRepository.findBySecurityGroupCodeAndApplicationCodeWithDetails(p.code, p.application);
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