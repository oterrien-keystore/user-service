package com.ote.mock;

import com.ote.common.persistence.model.*;
import com.ote.common.persistence.repository.*;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import com.ote.user.persistence.UserRightPersistenceServiceAdapter;
import com.ote.user.rights.api.PerimeterPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Profile("mockUserRightData")
@Service
@Slf4j
public class UserRightDataInitializerMock {

    @Autowired
    private IEncryptorService encryptorService;

    @Value("${spring.application.name}")
    private String applicationName;

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

    @Autowired
    private UserRightPersistenceServiceAdapter userRightPersistenceServiceAdapter;

    public UserRightDataInitializerMock() {
        log.warn("###### MOCK ##### " + this.getClass().getSimpleName());
    }

    @PostConstruct
    public void init() throws Exception {

        // Create Users
        createUsers(
                new User("olivier.terrien", "password"),
                new User("maryline.terrien", "password"));

        // Create Applications
        createApplications(applicationName, "TEST_SERVICE");

        // Create Perimeters
        createPerimeters(
                "APPLICATION", "USER", "PERIMETER", "PRIVILEGE",
                "DEAL", "DEAL/GLE");

        // Create Privileges
        createPrivileges("READ", "WRITE");

        // Create Security Group
        createSecurityGroup(new SecurityGroup("USER_RIGHT_CONTRIBUTORS", "olivier.terrien"));

        // Create Security Group Rights
        createSecurityGroupRight(new SecurityGroupRight("USER_RIGHT_CONTRIBUTORS", applicationName, "APPLICATION", "READ"));

        //Create User Rights
        createUserRights(
                new UserRight("olivier.terrien", applicationName, "APPLICATION", "WRITE"),
                new UserRight("olivier.terrien", applicationName, "USER", "READ"),
                new UserRight("olivier.terrien", applicationName, "PERIMETER", "READ"),
                new UserRight("olivier.terrien", applicationName, "PRIVILEGE", "READ"),
                new UserRight("olivier.terrien", "TEST_SERVICE", "DEAL", "READ"),
                new UserRight("olivier.terrien", "TEST_SERVICE", "DEAL/GLE", "WRITE"),
                new UserRight("maryline.terrien", "TEST_SERVICE", "DEAL/GLE", "READ")
        );

        log.info("###### MOCK ##### " + userRightPersistenceServiceAdapter.getPerimeters("olivier.terrien", applicationName).toString());
    }

    private void createUsers(User... users) {
        Arrays.asList(users).
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
        Arrays.asList(applications).
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
        Arrays.asList(perimeters).
                forEach(p -> {
                    PerimeterPath perimeterPath = new PerimeterPath.Parser(p).get();
                    AtomicReference<String> previous = new AtomicReference<>();
                    perimeterPath.forEach(pp -> {
                        PerimeterEntity perimeterEntity = new PerimeterEntity();
                        perimeterEntity.setCode(pp);
                        String parent = previous.get();
                        if (parent != null) {
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

    private void createPrivileges(String... privileges) {
        Arrays.asList(privileges).
                forEach(p -> {
                    PrivilegeEntity privilegeEntity = new PrivilegeEntity();
                    privilegeEntity.setCode(p);
                    if (!privilegeRepository.existsByCode(p)) {
                        privilegeRepository.save(privilegeEntity);
                        privilegeRepository.flush();
                    }
                });
    }

    private void createUserRights(UserRight... userRights) {
        Arrays.asList(userRights).
                forEach(p -> {
                    UserRightEntity userRightEntity = new UserRightEntity();
                    userRightEntity.setUser(userRepository.findByLogin(p.user));
                    userRightEntity.setApplication(applicationRepository.findByCode(p.application));
                    if (!userRightRepository.existsByUserLoginAndApplicationCode(p.user, p.application)) {
                        userRightRepository.save(userRightEntity);
                        userRightRepository.flush();
                    }
                    PerimeterPath perimeterPath = new PerimeterPath.Parser(p.perimeter).get();
                    String perimeter = perimeterPath.getPath()[perimeterPath.getPath().length - 1];
                    Set<PrivilegeEntity> privilegeEntities = p.privileges.stream().map(pp -> privilegeRepository.findByCode(pp)).collect(Collectors.toSet());

                    UserRightDetailEntity detail = new UserRightDetailEntity();
                    detail.setUserRight(userRightRepository.findByUserLoginAndApplicationCode(p.user, p.application));
                    detail.setPerimeter(perimeterRepository.findByCode(perimeter));
                    detail.setPrivileges(privilegeEntities);
                    userRightDetailRepository.save(detail);
                    userRightDetailRepository.flush();
                });
    }

    private void createSecurityGroup(SecurityGroup... securityGroups) {
        Arrays.asList(securityGroups).
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
        Arrays.asList(securityGroupRights).
                forEach(p -> {
                    SecurityGroupEntity securityGroup = securityGroupRepository.findByCode(p.code);
                    SecurityGroupRightEntity securityGroupRightEntity = new SecurityGroupRightEntity();
                    securityGroupRightEntity.setSecurityGroup(securityGroup);
                    securityGroupRightEntity.setApplication(applicationRepository.findByCode(p.application));
                    if (!securityGroupRightRepository.existsBySecurityGroupCodeAndApplicationCode(p.code, p.application)) {
                        securityGroupRightRepository.save(securityGroupRightEntity);
                        securityGroupRightRepository.flush();
                    }
                    PerimeterPath perimeterPath = new PerimeterPath.Parser(p.perimeter).get();
                    String perimeter = perimeterPath.getPath()[perimeterPath.getPath().length - 1];
                    Set<PrivilegeEntity> privilegeEntities = p.privileges.stream().map(pp -> privilegeRepository.findByCode(pp)).collect(Collectors.toSet());

                    SecurityGroupRightDetailEntity detail = new SecurityGroupRightDetailEntity();
                    detail.setSecurityGroupRight(securityGroupRightRepository.findBySecurityGroupCodeAndApplicationCode(p.code, p.application));
                    detail.setPerimeter(perimeterRepository.findByCode(perimeter));
                    detail.setPrivileges(privilegeEntities);
                    securityGroupRightDetailRepository.save(detail);
                    securityGroupRightDetailRepository.flush();
                });
    }

    private class SecurityGroup {
        private final String code;
        private final List<String> users;

        public SecurityGroup(String code, String... users) {
            this.code = code;
            this.users = Arrays.asList(users);
        }
    }

    private class SecurityGroupRight {
        private final String code;
        private final String application;
        private final String perimeter;
        private final List<String> privileges;

        public SecurityGroupRight(String code, String application, String perimeter, String... privileges) {
            this.code = code;
            this.application = application;
            this.perimeter = perimeter;
            this.privileges = Arrays.asList(privileges);
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
}