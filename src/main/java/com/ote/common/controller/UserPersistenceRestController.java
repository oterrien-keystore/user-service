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
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserPersistenceRestController implements IPersistenceRestController<UserPayload> {

    @Autowired
    @Getter
    private IPersistenceService<UserPayload> persistenceService;

    @Getter
    private String entityName = UserPayload.getEntityName();
}
