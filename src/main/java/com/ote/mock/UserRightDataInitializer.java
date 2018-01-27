package com.ote.mock;

import com.ote.common.persistence.model.*;
import com.ote.common.persistence.repository.*;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import com.ote.user.rights.api.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Profile("mockUserRightData")
@Service
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
    private IUserRightJpaRepository userRightRepository;

    @Autowired
    private IUserRightDetailJpaRepository userRightDetailRepository;

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
        createUsers(
                new User("olivier.terrien", "password"),
                new User("maryline.terrien", "password"));

        // Create Applications
        createApplications("TEST_SERVICE", "USER_SERVICE");

        // Create Perimeters
        createPerimeters("DEAL", "DEAL/GLE",
                "APPLICATION", "USER", "PERIMETER", "PRIVILEGE");

        // Create Privileges
        createPrivileges(
                new Privilege("ADMIN"),
                new Privilege("WRITE", "ADMIN"),
                new Privilege("READ", "WRITE"));

        //Create UserRights
        createUserRights(
                new UserRight("olivier.terrien", "TEST_SERVICE", "DEAL", "READ"),
                new UserRight("olivier.terrien", "TEST_SERVICE", "DEAL/GLE", "WRITE"),
                new UserRight("olivier.terrien", "USER_SERVICE", "APPLICATION", "WRITE"),
                new UserRight("olivier.terrien", "USER_SERVICE", "USER", "WRITE"),
                new UserRight("olivier.terrien", "USER_SERVICE", "PERIMETER", "WRITE"),
                new UserRight("olivier.terrien", "USER_SERVICE", "PRIVILEGE", "WRITE"));
    }

    public void clean() {
        log.warn("###### MOCK ##### Clean database");
        userRightDetailRepository.deleteAll();
        userRightRepository.deleteAll();
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

    private void createUserRights(UserRight... userRights) {
        Stream.of(userRights).
                peek(p -> {
                    String messageTemplate = "%s %s should exist";
                    assert userRepository.existsByLogin(p.user) : String.format(messageTemplate, "User", p.user);
                    assert applicationRepository.existsByCode(p.application) : String.format(messageTemplate, "Application", p.application);
                    assert perimeterRepository.existsByCode(p.perimeter) : String.format(messageTemplate, "Perimeter", p.perimeter);
                    p.privileges.forEach(pr -> {
                        assert privilegeRepository.existsByCode(pr) : String.format(messageTemplate, "Privilege", pr);
                    });
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
}