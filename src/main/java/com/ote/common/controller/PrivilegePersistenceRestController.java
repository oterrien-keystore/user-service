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
@RequestMapping("/api/v1/privileges")
@Slf4j
@Validated
public class PrivilegePersistenceRestController implements IPersistenceRestController<PrivilegePayload> {

    @Autowired
    @Getter
    private IPersistenceService<PrivilegePayload> persistenceService;

    @Getter
    private String entityName = PrivilegePayload.getEntityName();
}
