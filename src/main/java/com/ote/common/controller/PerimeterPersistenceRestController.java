package com.ote.common.controller;

import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perimeters")
@Slf4j
@Validated
public class PerimeterPersistenceRestController implements IPersistenceRestController<PerimeterPayload> {

    @Autowired
    @Getter
    private IPersistenceService<PerimeterPayload> persistenceService;

    @Getter
    private String entityName = PerimeterPayload.getEntityName();
}
