package com.ote.user.controller;

import com.ote.common.persistence.model.UserEntity;
import com.ote.user.crud.UserPayload;
import com.ote.user.persistence.UserPersistenceService;
import com.ote.user.service.UserMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserRestController {

    @Autowired
    private UserMapperService userMapperService;

    @Autowired
    private UserPersistenceService userPersistenceService;

    // region CREATE
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserPayload create(@Valid @RequestBody UserPayload user) {
        log.info("create user");

        UserEntity entity = userMapperService.convert(user);
        entity = userPersistenceService.create(entity);
        return userMapperService.convert(entity);
    }
    // endregion

    //region READ
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserPayload get(@PathVariable("id") int id, @RequestParam(value = "withRights", defaultValue = "False") boolean withRights) {
        throw new NotImplementedException();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<UserPayload> get(@ModelAttribute UserPayload userPayloadFilter,
                                 @RequestParam(required = false) String sortingBy,
                                 @RequestParam(required = false, defaultValue = "ASC") String sortingDirection,
                                 @RequestParam(required = false, defaultValue = "${page.default.size}") int pageSize,
                                 @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                 @RequestParam(defaultValue = "False") boolean withRights) {

        log.info("get user where filter is " + userPayloadFilter);
        throw new NotImplementedException();
    }
    //endregion

    // region UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK) // PAY ATTENTION TO NOT USE RESET_CONTENT which does not allow any response body
    @ResponseBody
    public UserPayload update(@PathVariable("id") int id, @Valid @RequestBody UserPayload user) {
        log.info("update user where id " + id);
        throw new NotImplementedException();
    }
    // endregion

    // region DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        log.info("delete user where id " + id);
        throw new NotImplementedException();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@ModelAttribute UserPayload userPayloadFilter) {

        log.info("delete user where filter is " + userPayloadFilter);
        throw new NotImplementedException();
    }
    //endregion
}
