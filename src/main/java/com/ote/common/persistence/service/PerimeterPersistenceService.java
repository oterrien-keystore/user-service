package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.PerimeterPayload;
import com.ote.common.persistence.model.PerimeterEntity;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class PerimeterPersistenceService extends AdtPersistenceService<PerimeterPayload, PerimeterEntity> {

    @Getter
    private String scope = Scope.Perimeter.name();
}
