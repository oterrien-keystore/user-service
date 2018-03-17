package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.payload.UserPayload;
import com.ote.common.persistence.model.UserEntity;
import com.ote.common.persistence.repository.IUserJpaRepository;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserPersistenceRestControllerService implements IPersistenceRestController<UserPayload> {

    @Getter
    private final IPersistenceService<UserPayload> persistenceService;

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
    public void addSecurityGroup(Long id, String securityGroup) {

    }

    public void removeSecurityGroup(Long id, String securityGroup) {

    }
    //endregion
}
