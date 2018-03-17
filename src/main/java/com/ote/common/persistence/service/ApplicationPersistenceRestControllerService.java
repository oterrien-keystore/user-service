package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.payload.ApplicationPayload;
import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.common.persistence.repository.IApplicationJpaRepository;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPersistenceRestControllerService implements IPersistenceRestController<ApplicationPayload> {

    @Getter
    private final IPersistenceService<ApplicationPayload> persistenceService;

    @Getter
    private final String scope = Scope.Application.name();

    public ApplicationPersistenceRestControllerService(@Autowired IApplicationJpaRepository repository,
                                                       @Value("${page.default.size}") int defaultPageSize) {

        this.persistenceService = new IPersistenceService<ApplicationPayload>() {
            @Override
            public IEntityRepository<ApplicationEntity> getEntityRepository() {
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
