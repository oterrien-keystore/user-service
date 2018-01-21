package com.ote.common.persistence.service;

import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceService;
import com.ote.crud.model.IEntity;
import com.ote.crud.model.IPayload;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AdtPersistenceService<TP extends IPayload, TE extends IEntity> implements IPersistenceService<TP> {

    @Autowired
    @Getter
    private IEntityRepository<TE> entityRepository;

    @Value("${page.default.size}")
    @Getter
    private int defaultPageSize;
}
