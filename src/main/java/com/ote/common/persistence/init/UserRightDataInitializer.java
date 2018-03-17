package com.ote.common.persistence.init;

import com.ote.common.persistence.model.*;
import com.ote.common.persistence.repository.*;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import com.ote.user.rights.api.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(name = "spring.datasource.initialize", havingValue = "true")
@Slf4j
public class UserRightDataInitializer {

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

    public UserRightDataInitializer() {
        log.warn("###### MOCK ##### ");
    }

    @PostConstruct
    public void postConstruct() {
        clean();
        init();
    }

    public void init() {
        log.warn("###### MOCK ##### Init database with samples");

        // Create Users
        createUsers(new User("olivier.terrien", "password"),
                new User("steve.jobs", "password"));

        // Create Applications
        createApplications("TEST_SERVICE", "USER_SERVICE");

        // Create Perimeters
        createPerimeters("APPLICATION", "USER", "PERIMETER", "PRIVILEGE", "SECURITY_GROUP");

        // Create Privileges
        createPrivileges(
                new Privilege("ADMIN"),
                new Privilege("WRITE", "ADMIN"),
                new Privilege("READ", "WRITE"));

        // Create Security Group
        createSecurityGroup("USER_SERVICE_ADMINS");

        addUsersToSecurityGroup("USER_SERVICE_ADMINS", "olivier.terrien");

        // Create Security Group Rights
        createSecurityGroupRight(
                new SecurityGroupRight("USER_SERVICE_ADMINS", "USER_SERVICE", "APPLICATION", "ADMIN"),
                new SecurityGroupRight("USER_SERVICE_ADMINS", "USER_SERVICE", "USER", "ADMIN"),
                new SecurityGroupRight("USER_SERVICE_ADMINS", "USER_SERVICE", "PRIVILEGE", "ADMIN"),
                new SecurityGroupRight("USER_SERVICE_ADMINS", "USER_SERVICE", "PERIMETER", "ADMIN"),
                new SecurityGroupRight("USER_SERVICE_ADMINS", "USER_SERVICE", "SECURITY_GROUP", "ADMIN"));
    }

    public void clean() {
        try {
            log.warn("###### MOCK ##### Clean database");
            securityGroupRightDetailRepository.deleteAll();
            securityGroupRightRepository.deleteAll();
            userRepository.deleteAll();
            securityGroupRepository.deleteAll();
            applicationRepository.deleteAll();
            perimeterRepository.deleteAll();
            privilegeRepository.deleteAll();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
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
                peek(p -> {
                    assert securityGroupRepository.existsByCode(p) : "SecurityGroup " + p + " should exist";
                }).
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