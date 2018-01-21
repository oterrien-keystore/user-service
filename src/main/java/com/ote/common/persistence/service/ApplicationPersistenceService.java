package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.ApplicationPayload;
import com.ote.common.persistence.model.ApplicationEntity;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPersistenceService extends AdtPersistenceService<ApplicationPayload, ApplicationEntity> {

    @Getter
    private String scope = Scope.Application.name();
}
