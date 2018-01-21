package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.PrivilegePayload;
import com.ote.common.persistence.model.PrivilegeEntity;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class PrivilegePersistenceService extends AdtPersistenceService<PrivilegePayload, PrivilegeEntity> {

    @Getter
    private String scope = Scope.Privilege.name();
}
