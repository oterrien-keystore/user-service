package com.ote.common.controller;

import com.ote.common.Scope;
import com.ote.crud.DefaultPersistenceRestController;
import com.ote.crud.IPersistenceRestController;
import com.ote.crud.IPersistenceService;
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

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserPersistenceRestController {

    @Autowired
    private IPersistenceService<UserPayload> persistenceService;

    // NB: when the current RestController implements the interface, endpoints are not visible -> use a delegate
    private final IPersistenceRestController<UserPayload> defaultController;

    public UserPersistenceRestController() {
        defaultController = new DefaultPersistenceRestController<>(persistenceService, Scope.User.name());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'READ')")
    public UserPayload get(@PathVariable("id") long id) throws NotFoundException {
        return defaultController.get(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'READ')")
    public SplitList<UserPayload> get(@RequestParam(required = false) Filters filters,
                                      @RequestParam(required = false) SortingParameters sortingParameters,
                                      @RequestParam(required = false) SplitListParameter splitListParam) {
        return defaultController.get(filters, sortingParameters, splitListParam);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public UserPayload reset(@PathVariable("id") long id,
                             @RequestBody UserPayload payload) throws ResetException, NotFoundException {
        return defaultController.reset(id, payload);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public UserPayload merge(@PathVariable("id") long id,
                             @RequestBody UserPayload payload) throws MergeException, NotFoundException {
        return defaultController.merge(id, payload);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public UserPayload create(@RequestBody UserPayload payload) throws CreateException {
        return defaultController.create(payload);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public void delete(@PathVariable("id") long id) {
        defaultController.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('USER', 'WRITE')")
    public void delete(@RequestParam(required = false) Filters filters) {
        defaultController.delete(filters);
    }
}
