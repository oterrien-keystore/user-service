package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.SecurityGroupPayload;
import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityGroupPersistenceService implements IPersistenceService<SecurityGroupPayload> {

    @Autowired
    @Getter
    private IEntityRepository<SecurityGroupEntity> entityRepository;

    @Getter
    private String scope = Scope.SecurityGroup.name();

    @Value("${page.default.size}")
    @Getter
    private int defaultPageSize;
}
