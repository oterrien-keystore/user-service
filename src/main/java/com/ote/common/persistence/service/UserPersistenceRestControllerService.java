package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.SecurityGroupPersistenceRestController;
import com.ote.common.payload.SecurityGroupPayload;
import com.ote.common.payload.UserPayload;
import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.common.persistence.model.UserEntity;
import com.ote.common.persistence.repository.ISecurityGroupJpaRepository;
import com.ote.common.persistence.repository.IUserJpaRepository;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import com.ote.crud.exception.NotFoundException;
import com.ote.user.rights.api.exception.SecurityGroupNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPersistenceRestControllerService implements IPersistenceRestController<UserPayload> {

    @Getter
    private final IPersistenceService<UserPayload> persistenceService;

    @Autowired
    private ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private SecurityGroupPersistenceRestController securityGroupPersistenceRestController;

    @Getter
    private final String scope = Scope.User.name();

    public UserPersistenceRestControllerService(@Autowired IUserJpaRepository repository,
                                                @Value("${page.default.size}") int defaultPageSize) {

        this.persistenceService = new IPersistenceService<UserPayload>() {
            @Override
            public IEntityRepository<UserEntity> getEntityRepository() {
                return repository;
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

    //region >>> add and remove securityGroups <<<
    public List<SecurityGroupPayload> getSecurityGroups(long id) throws NotFoundException {
        UserPayload userPayload = this.get(id);

        return securityGroupRepository.findByUser(userPayload.getLogin()).
                stream().
                map(SecurityGroupEntity::convert).
                collect(Collectors.toList());
    }

    public void addSecurityGroup(long id, String securityGroup) throws NotFoundException {

        UserPayload userPayload = this.get(id);

        try {
            assertSecurityGroupFound(securityGroup);
        } catch (SecurityGroupNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(securityGroup);
        if (securityGroupEntity.getUsers().stream().noneMatch(p -> p.getLogin().equalsIgnoreCase(userPayload.getLogin()))) {
            securityGroupPersistenceRestController.addUser(securityGroupEntity.getId(), userPayload.getLogin());
            securityGroupRepository.save(securityGroupEntity);
        }
    }

    public void removeSecurityGroup(long id, String securityGroup) throws NotFoundException {

        UserPayload userPayload = this.get(id);

        try {
            assertSecurityGroupFound(securityGroup);
        } catch (SecurityGroupNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(securityGroup);
        if (securityGroupEntity.getUsers().stream().anyMatch(p -> p.getLogin().equalsIgnoreCase(userPayload.getLogin()))) {
            securityGroupEntity.getUsers().removeIf(p -> p.getLogin().equalsIgnoreCase(userPayload.getLogin()));
            securityGroupRepository.save(securityGroupEntity);
        }
    }
    //endregion

    private void assertSecurityGroupFound(String securityGroup) throws SecurityGroupNotFoundException {
        if (!securityGroupRepository.existsByCode(securityGroup)) {
            throw new SecurityGroupNotFoundException(securityGroup);
        }
    }
}
