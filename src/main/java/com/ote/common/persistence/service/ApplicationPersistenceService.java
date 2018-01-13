package com.ote.common.persistence.service;

import com.ote.common.controller.ApplicationPayload;
import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPersistenceService implements IPersistenceService<ApplicationPayload> {

    @Autowired
    @Getter
    private IEntityRepository<ApplicationEntity> entityRepository;

    @Getter
    private String entityName = "APPLICATION";

    @Value("${page.default.size}")
    @Getter
    private int defaultPageSize;
}
