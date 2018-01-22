package com.ote.common.controller;

import com.ote.common.persistence.service.UserRightPersistenceService;
import com.ote.crud.exception.NotFoundException;
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
@RequestMapping("/api/v1/user-rights")
@Slf4j
@Validated
public class UserRightPeristenceRestController {

    @Autowired
    private UserRightPersistenceService userRightPersistenceService;

    @GetMapping(value = "/users/{user}/applications/{application}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasPermission('USER_RIGHT', 'READ')")
    public List<UserRightDetailPayload> get(@PathVariable("user") String user,
                                                 @PathVariable("application") String application,
                                                 Filters filters,
                                                 SortingParameters sortingParameters,
                                                 SplitListParameter splitListParam) {
        return userRightPersistenceService.findMany(user, application, filters, sortingParameters, splitListParam);
    }
}
