package com.ote.common.persistence.service;

import com.ote.common.controller.PrivilegePayload;
import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PrivilegePersistenceService implements IPersistenceService<PrivilegePayload> {

    @Autowired
    @Getter
    private IEntityRepository<PrivilegeEntity> entityRepository;

    @Getter
    private String entityName = "PRIVILEGE";

    @Value("${page.default.size}")
    @Getter
    private int defaultPageSize;
}
