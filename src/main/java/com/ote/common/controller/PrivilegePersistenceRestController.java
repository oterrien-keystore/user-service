package com.ote.common.controller;

import com.ote.common.payload.PrivilegePayload;
import com.ote.common.persistence.service.PrivilegePersistenceRestControllerService;
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

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/privileges")
@Slf4j
@Validated
public class PrivilegePersistenceRestController {

    @Autowired
    private PrivilegePersistenceRestControllerService persistenceService;

    //region >>> Persistence <<<
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('PRIVILEGE', 'READ')")
    public PrivilegePayload get(@PathVariable("id") long id) throws NotFoundException {
        return persistenceService.get(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('PRIVILEGE', 'READ')")
    public SplitList<PrivilegePayload> get(@RequestParam(required = false) Filters filters,
                                           @RequestParam(required = false) SortingParameters sortingParameters,
                                           @RequestParam(required = false) SplitListParameter splitListParam) {
        return persistenceService.get(filters, sortingParameters, splitListParam);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('PRIVILEGE', 'WRITE')")
    public PrivilegePayload reset(@PathVariable("id") long id,
                                  @RequestBody @NotNull @Validated(IPayload.ResettingValidationType.class) PrivilegePayload payload) throws ResetException, NotFoundException {
        return persistenceService.reset(id, payload);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('PRIVILEGE', 'WRITE')")
    public PrivilegePayload merge(@PathVariable("id") long id,
                                  @RequestBody @NotNull @Validated(IPayload.MergingValidationType.class) PrivilegePayload payload) throws MergeException, NotFoundException {
        return persistenceService.merge(id, payload);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('PRIVILEGE', 'WRITE')")
    public PrivilegePayload create(@RequestBody @NotNull @Validated(IPayload.CreatingValidationType.class) PrivilegePayload payload) throws CreateException {
        return persistenceService.create(payload);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('PRIVILEGE', 'WRITE')")
    public void delete(@PathVariable("id") long id) {
        persistenceService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('PRIVILEGE', 'ADMIN')")
    public void delete(@RequestParam(required = false) Filters filters) {
        persistenceService.delete(filters);
    }
    //endregion
}
