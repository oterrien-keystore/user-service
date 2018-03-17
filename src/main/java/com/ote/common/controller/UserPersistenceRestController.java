package com.ote.common.controller;

import com.ote.common.payload.UserPayload;
import com.ote.common.persistence.service.UserPersistenceRestControllerService;
import com.ote.crud.exception.CreateException;
import com.ote.crud.exception.MergeException;
import com.ote.crud.exception.NotFoundException;
import com.ote.crud.exception.ResetException;
import com.ote.crud.model.Filters;
import com.ote.crud.model.SortingParameters;
import com.ote.crud.model.SplitList;
import com.ote.crud.model.SplitListParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserPersistenceRestController {

    @Autowired
    private UserPersistenceRestControllerService persistenceService;

    //region >>> Persistence <<<
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'READ')")
    public UserPayload get(@PathVariable("id") long id) throws NotFoundException {
        return persistenceService.get(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'READ')")
    public SplitList<UserPayload> get(@RequestParam(required = false) Filters filters,
                                      @RequestParam(required = false) SortingParameters sortingParameters,
                                      @RequestParam(required = false) SplitListParameter splitListParam) {
        return persistenceService.get(filters, sortingParameters, splitListParam);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public UserPayload reset(@PathVariable("id") long id,
                             @RequestBody UserPayload payload) throws ResetException, NotFoundException {
        return persistenceService.reset(id, payload);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public UserPayload merge(@PathVariable("id") long id,
                             @RequestBody UserPayload payload) throws MergeException, NotFoundException {
        return persistenceService.merge(id, payload);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public UserPayload create(@RequestBody UserPayload payload) throws CreateException {
        return persistenceService.create(payload);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public void delete(@PathVariable("id") long id) {
        persistenceService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public void delete(@RequestParam(required = false) Filters filters) {
        persistenceService.delete(filters);
    }
    //endregion

    //region >>> add securityGroups<<<
    @PutMapping(value = "/{id}/securityGroups")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public void addGroups(@PathVariable("id") Long id, @RequestBody String securityGroup) throws NotFoundException {
        persistenceService.addSecurityGroup(id, securityGroup);
    }

    @DeleteMapping(value = "/{id}/securityGroups")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public void removeUsers(@PathVariable("id") Long id, @RequestBody String securityGroup) throws NotFoundException {
        persistenceService.removeSecurityGroup(id, securityGroup);
    }
    //endregion
}
