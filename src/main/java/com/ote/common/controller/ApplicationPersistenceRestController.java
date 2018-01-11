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
@RequestMapping("/api/v1/applications")
@Slf4j
@Validated
public class ApplicationPersistenceRestController implements IPersistenceRestController<ApplicationPayload> {

    @Autowired
    @Getter
    private IPersistenceService<ApplicationPayload> persistenceService;

    @Getter
    private String entityName = ApplicationPayload.getEntityName();
}
