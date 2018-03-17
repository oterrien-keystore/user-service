package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.payload.RightPayload;
import com.ote.common.payload.SecurityGroupPayload;
import com.ote.common.payload.UserPayload;
import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.common.persistence.model.SecurityGroupRightDetailEntity;
import com.ote.common.persistence.model.SecurityGroupRightEntity;
import com.ote.common.persistence.model.UserEntity;
import com.ote.common.persistence.repository.*;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import com.ote.crud.exception.NotFoundException;
import com.ote.user.rights.api.exception.ApplicationNotFoundException;
import com.ote.user.rights.api.exception.PerimeterNotFoundException;
import com.ote.user.rights.api.exception.PrivilegeNotFoundException;
import com.ote.user.rights.api.exception.UserNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityGroupPersistenceRestControllerService implements IPersistenceRestController<SecurityGroupPayload> {

    @Getter
    private final IPersistenceService<SecurityGroupPayload> persistenceService;

    @Getter
    private final String scope = Scope.SecurityGroup.name();

    private final ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private ISecurityGroupRightJpaRepository securityGroupRightRepository;

    @Autowired
    private ISecurityGroupRightDetailJpaRepository securityGroupRightDetailRepository;

    @Autowired
    private IApplicationJpaRepository applicationRepository;

    @Autowired
    private IPerimeterJpaRepository perimeterRepository;

    @Autowired
    private IPrivilegeJpaRepository privilegeRepository;

    @Autowired
    private IUserJpaRepository userRepository;

    public SecurityGroupPersistenceRestControllerService(@Autowired ISecurityGroupJpaRepository securityGroupRepository,
                                                         @Value("${page.default.size}") int defaultPageSize) {

        this.securityGroupRepository = securityGroupRepository;
        this.persistenceService = new IPersistenceService<SecurityGroupPayload>() {
            @Override
            public IEntityRepository<SecurityGroupEntity> getEntityRepository() {
                return securityGroupRepository;
            }

            @Override
            public String getScope() {
                return scope;
            }

            @Override
            public int getDefaultPageSize() {
                return defaultPageSize;
            }
        };
    }

    public SecurityGroupPayload get(long id, boolean withDefaults) throws NotFoundException {

        SecurityGroupPayload result = this.get(id);
        if (withDefaults) {
            securityGroupRightRepository.findBySecurityGroupCodeWithDetails(result.getCode()).
                    forEach(p -> {
                        p.getDetails().
                                forEach(p1 -> {
                                    p1.getPrivileges().forEach(p2 -> {
                                        RightPayload p3 = new RightPayload();
                                        p3.setApplication(p.getApplication().getCode());
                                        p3.setPerimeter(p1.getPerimeter().getCode());
                                        p3.setPrivilege(p2.getCode());
                                        result.getRights().add(p3);
                                    });

                                });
                    });
        }
        return result;
    }

    //region >>> add and remove rights <<<
    public List<RightPayload> getRights(Long id) throws NotFoundException {

        SecurityGroupEntity securityGroupEntity = Optional.ofNullable(securityGroupRepository.findOne(id)).orElseThrow(() -> new NotFoundException(scope, id));

        List<SecurityGroupRightEntity> securityGroupRights = securityGroupRightRepository.findBySecurityGroupCodeWithDetails(securityGroupEntity.getCode());

        List<RightPayload> result = new ArrayList<>();

        securityGroupRights.forEach(securityGroupRightEntity -> {
            String application = securityGroupRightEntity.getApplication().getCode();
            securityGroupRightEntity.getDetails().forEach(detail -> {
                String perimeter = detail.getPerimeter().getCode();
                detail.getPrivileges().forEach(privilege -> {
                    RightPayload rightPayload = new RightPayload();
                    rightPayload.setApplication(application);
                    rightPayload.setPerimeter(perimeter);
                    rightPayload.setPrivilege(privilege.getCode());
                    result.add(rightPayload);
                });
            });
        });

        return result;
    }

    public void addRight(long id, RightPayload right) throws NotFoundException {

        SecurityGroupEntity securityGroupEntity = Optional.ofNullable(securityGroupRepository.findOne(id)).orElseThrow(() -> new NotFoundException(scope, id));

        try {
            assertApplicationFound(right.getApplication());
            assertPerimeterFound(right.getPerimeter());
            assertPrivilegeFound(right.getPrivilege());
        } catch (ApplicationNotFoundException | PerimeterNotFoundException | PrivilegeNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }

        SecurityGroupRightEntity securityGroupRightEntity = Optional.ofNullable(securityGroupRightRepository.findBySecurityGroupCodeAndApplicationCodeWithDetails(securityGroupEntity.getCode(), right.getApplication())).
                orElseGet(() -> {
                    SecurityGroupRightEntity a = new SecurityGroupRightEntity();
                    a.setSecurityGroup(securityGroupEntity);
                    a.setApplication(applicationRepository.findByCode(right.getApplication()));
                    a.setDetails(new HashSet<>());
                    return securityGroupRightRepository.saveAndFlush(a);
                });

        SecurityGroupRightDetailEntity securityGroupRightDetailEntity =
                securityGroupRightEntity.getDetails().
                        stream().
                        filter(p -> p.getPerimeter().getCode().equalsIgnoreCase(right.getPerimeter())).
                        findAny().
                        orElseGet(() -> {
                            SecurityGroupRightDetailEntity a = new SecurityGroupRightDetailEntity();
                            a.setSecurityGroupRight(securityGroupRightEntity);
                            a.setPerimeter(perimeterRepository.findByCode(right.getPerimeter()));
                            a.setPrivileges(new HashSet<>());
                            return a;
                        });

        if (securityGroupRightDetailEntity.getPrivileges().stream().noneMatch(p -> p.getCode().equalsIgnoreCase(right.getPrivilege()))) {
            securityGroupRightDetailEntity.getPrivileges().add(privilegeRepository.findByCode(right.getPrivilege()));
            securityGroupRightDetailRepository.saveAndFlush(securityGroupRightDetailEntity);
        }
    }

    public void removeRight(long id, RightPayload right) throws NotFoundException {

        SecurityGroupEntity securityGroupEntity = Optional.ofNullable(securityGroupRepository.findOne(id)).orElseThrow(() -> new NotFoundException(scope, id));

        try {
            assertApplicationFound(right.getApplication());
            assertPerimeterFound(right.getPerimeter());
            assertPrivilegeFound(right.getPrivilege());
        } catch (ApplicationNotFoundException | PerimeterNotFoundException | PrivilegeNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }

        Optional.ofNullable(securityGroupRightRepository.findBySecurityGroupCodeAndApplicationCodeWithDetails(securityGroupEntity.getCode(), right.getApplication())).
                ifPresent(p -> {
                    if (p.getDetails().stream().anyMatch(p1 -> p1.getPerimeter().getCode().equalsIgnoreCase(right.getPerimeter()))) {
                        p.getDetails().removeIf(p1 -> p1.getPerimeter().getCode().equalsIgnoreCase(right.getPerimeter()));
                        if (p.getDetails().isEmpty()) {
                            securityGroupRightRepository.delete(p.getId());
                        } else {
                            securityGroupRightRepository.saveAndFlush(p);
                        }
                    }
                });
    }
    //endregion

    //region >>> add and remove users <<<
    public List<String> getUsers(Long id) throws NotFoundException {

        SecurityGroupEntity securityGroupEntity = Optional.ofNullable(securityGroupRepository.findOne(id)).orElseThrow(() -> new NotFoundException(scope, id));
        return securityGroupEntity.getUsers().stream().map(UserEntity::getLogin).collect(Collectors.toList());
    }

    public void addUser(Long id, String user) throws NotFoundException {

        SecurityGroupEntity securityGroupEntity = Optional.ofNullable(securityGroupRepository.findOne(id)).orElseThrow(() -> new NotFoundException(scope, id));

        try {
            assertUserFound(user);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }

        UserEntity userEntity = userRepository.findByLogin(user);

        if (securityGroupEntity.getUsers().stream().noneMatch(p -> p.getId() == userEntity.getId())) {
            securityGroupEntity.getUsers().add(userEntity);
            securityGroupRepository.saveAndFlush(securityGroupEntity);
        }
    }

    public void removeUser(Long id, String user) throws NotFoundException {

        SecurityGroupEntity securityGroupEntity = Optional.ofNullable(securityGroupRepository.findOne(id)).orElseThrow(() -> new NotFoundException(scope, id));

        try {
            assertUserFound(user);
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }

        UserEntity userEntity = userRepository.findByLogin(user);
        if (securityGroupEntity.getUsers().stream().anyMatch(p -> p.getId() == userEntity.getId())) {
            securityGroupEntity.getUsers().remove(userEntity);
            securityGroupRepository.saveAndFlush(securityGroupEntity);
        }
    }
    //endregion

    private void assertApplicationFound(String application) throws ApplicationNotFoundException {
        if (!applicationRepository.existsByCode(application)) {
            throw new ApplicationNotFoundException(application);
        }
    }

    private void assertPerimeterFound(String perimeter) throws PerimeterNotFoundException {
        if (!perimeterRepository.existsByCode(perimeter)) {
            throw new PerimeterNotFoundException(perimeter);
        }
    }

    private void assertPrivilegeFound(String privilege) throws PrivilegeNotFoundException {
        if (!privilegeRepository.existsByCode(privilege)) {
            throw new PrivilegeNotFoundException(privilege);
        }
    }

    private void assertUserFound(String login) throws UserNotFoundException {
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException(login);
        }
    }
}
