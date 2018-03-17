package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.payload.PrivilegePayload;
import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.common.persistence.repository.IPrivilegeJpaRepository;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PrivilegePersistenceRestControllerService implements IPersistenceRestController<PrivilegePayload> {

    @Getter
    private final IPersistenceService<PrivilegePayload> persistenceService;

    @Getter
    private final String scope = Scope.Privilege.name();

    public PrivilegePersistenceRestControllerService(@Autowired IPrivilegeJpaRepository repository,
                                                     @Value("${page.default.size}") int defaultPageSize) {

        this.persistenceService = new IPersistenceService<PrivilegePayload>() {
            @Override
            public IEntityRepository<PrivilegeEntity> getEntityRepository() {
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
}
