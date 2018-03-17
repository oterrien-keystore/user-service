package com.ote.common.controller;

import com.ote.common.payload.RightPayload;
import com.ote.common.payload.SecurityGroupPayload;
import com.ote.common.persistence.service.SecurityGroupPersistenceRestControllerService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/securityGroups")
@Slf4j
@Validated
public class SecurityGroupPersistenceRestController {

    @Autowired
    private SecurityGroupPersistenceRestControllerService persistenceService;

    //region >>> Persistence <<<
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'READ')")
    public SecurityGroupPayload get(@PathVariable("id") long id, @RequestParam(value = "withRights", defaultValue = "False") boolean withRights) throws NotFoundException {
        return persistenceService.get(id, withRights);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'READ')")
    public SplitList<SecurityGroupPayload> get(@RequestParam(required = false) Filters filters,
                                               @RequestParam(required = false) SortingParameters sortingParameters,
                                               @RequestParam(required = false) SplitListParameter splitListParam) {
        return persistenceService.get(filters, sortingParameters, splitListParam);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public SecurityGroupPayload reset(@PathVariable("id") long id,
                                      @RequestBody @NotNull @Validated(IPayload.ResettingValidationType.class) SecurityGroupPayload payload) throws ResetException, NotFoundException {
        return persistenceService.reset(id, payload);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public SecurityGroupPayload merge(@PathVariable("id") long id,
                                      @RequestBody @NotNull @Validated(IPayload.MergingValidationType.class) SecurityGroupPayload payload) throws MergeException, NotFoundException {
        return persistenceService.merge(id, payload);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public SecurityGroupPayload create(@RequestBody @NotNull @Validated(IPayload.CreatingValidationType.class) SecurityGroupPayload payload) throws CreateException {
        return persistenceService.create(payload);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void delete(@PathVariable("id") long id) {
        persistenceService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'ADMIN')")
    public void delete(@RequestParam(required = false) Filters filters) {
        persistenceService.delete(filters);
    }
    //endregion

    //region >>> add rights <<<
    @GetMapping(value = "/{id}/rights")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'READ')")
    public List<RightPayload> getRights(@PathVariable("id") Long id) throws NotFoundException {
        return persistenceService.getRights(id);
    }

    @PutMapping(value = "/{id}/rights")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void addRight(@PathVariable("id") Long id, @RequestBody @Valid RightPayload payload) throws NotFoundException {
        persistenceService.addRight(id, payload);
    }

    @DeleteMapping(value = "/{id}/rights")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void removeRight(@PathVariable("id") Long id, @RequestBody @Valid RightPayload payload) throws NotFoundException {
        persistenceService.removeRight(id, payload);
    }
    // endregion

    //region >>> add users <<<
    @GetMapping(value = "/{id}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'READ')")
    public List<String> getUsers(@PathVariable("id") Long id) throws NotFoundException {
        return persistenceService.getUsers(id);
    }

    @PutMapping(value = "/{id}/users", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void addUsers(@PathVariable("id") Long id, @RequestBody String user) throws NotFoundException {
        persistenceService.addUser(id, user);
    }

    @DeleteMapping(value = "/{id}/users", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @PreAuthorize("hasPermission('SECURITY_GROUP', 'WRITE')")
    public void removeUsers(@PathVariable("id") Long id, @RequestBody String user) throws NotFoundException {
        persistenceService.removeUser(id, user);
    }
    //endregion
}