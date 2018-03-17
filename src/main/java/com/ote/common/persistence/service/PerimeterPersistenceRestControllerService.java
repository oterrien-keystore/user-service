package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.payload.PerimeterPayload;
import com.ote.common.persistence.model.PerimeterEntity;
import com.ote.common.persistence.repository.IPerimeterJpaRepository;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PerimeterPersistenceRestControllerService implements IPersistenceRestController<PerimeterPayload> {

    @Getter
    private final IPersistenceService<PerimeterPayload> persistenceService;

    @Getter
    private final String scope = Scope.Perimeter.name();

    public PerimeterPersistenceRestControllerService(@Autowired IPerimeterJpaRepository repository,
                                                     @Value("${page.default.size}") int defaultPageSize) {

        this.persistenceService = new IPersistenceService<PerimeterPayload>() {
            @Override
            public IEntityRepository<PerimeterEntity> getEntityRepository() {
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
