package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.SecurityGroupPayload;
import com.ote.common.persistence.model.SecurityGroupEntity;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SecurityGroupPersistenceService extends AdtPersistenceService<SecurityGroupPayload, SecurityGroupEntity> {

    @Getter
    private String scope = Scope.SecurityGroup.name();
}
