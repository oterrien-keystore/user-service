package com.ote.common.persistence.service;

import com.ote.common.controller.PerimeterPayload;
import com.ote.common.persistence.model.PerimeterEntity;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PerimeterPersistenceService implements IPersistenceService<PerimeterPayload> {

    @Autowired
    @Getter
    private IEntityRepository<PerimeterEntity> entityRepository;

    @Getter
    private String entityName = PerimeterPayload.getEntityName();

    @Value("${page.default.size}")
    @Getter
    private int defaultPageSize;
}
