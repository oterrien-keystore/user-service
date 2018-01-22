package com.ote.common.controller;

import com.ote.common.Scope;
import com.ote.crud.DefaultPersistenceRestController;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
import com.ote.crud.exception.CreateException;
import com.ote.crud.exception.MergeException;
import com.ote.crud.exception.NotFoundException;
import com.ote.crud.exception.ResetException;
import com.ote.crud.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/securityGroups")
@Slf4j
@Validated
public class SecurityGroupPersistenceRestController {

    @Autowired
    public IPersistenceService<SecurityGroupPayload> persistenceService;

    // NB: when the current RestController implements the interface, endpoints are not visible -> use a delegate
    private final IPersistenceRestController<SecurityGroupPayload> defaultController;

    public SecurityGroupPersistenceRestController() {
        defaultController = new DefaultPersistenceRestController<>(persistenceService, Scope.SecurityGroup.name());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'READ')")
    public SecurityGroupPayload get(@PathVariable("id") long id) throws NotFoundException {
        return defaultController.get(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'READ')")
    public SplitList<SecurityGroupPayload> get(@RequestParam(required = false) Filters filters,
                                               @RequestParam(required = false) SortingParameters sortingParameters,
                                               @RequestParam(required = false) SplitListParameter splitListParam) {
        return defaultController.get(filters, sortingParameters, splitListParam);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public SecurityGroupPayload reset(@PathVariable("id") long id,
                                      @RequestBody @NotNull @Validated(IPayload.ResettingValidationType.class) SecurityGroupPayload payload) throws ResetException, NotFoundException {
        return defaultController.reset(id, payload);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public SecurityGroupPayload merge(@PathVariable("id") long id,
                                      @RequestBody @NotNull @Validated(IPayload.MergingValidationType.class) SecurityGroupPayload payload) throws MergeException, NotFoundException {
        return defaultController.merge(id, payload);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public SecurityGroupPayload create(@RequestBody @NotNull @Validated(IPayload.CreatingValidationType.class) SecurityGroupPayload payload) throws CreateException {
        return defaultController.create(payload);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void delete(@PathVariable("id") long id) {
        defaultController.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void delete(@RequestParam(required = false) Filters filters) {
        defaultController.delete(filters);
    }
}
