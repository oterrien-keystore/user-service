package com.ote.rights.controller;

import com.ote.crud.exception.NotFoundException;
import com.ote.rights.service.UserRightServiceAdapter;
import com.ote.user.rights.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rights")
@Slf4j
public class UserRightRestController {

    @Autowired
    private UserRightServiceAdapter userRightService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(@RequestParam("user") String user,
                                                                 @RequestParam("application") String application,
                                                                 @RequestParam("perimeter") String perimeter,
                                                                 @RequestParam("privilege") String privilege) throws NotFoundException {
        try {
            return userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
        } catch (ApplicationNotFoundException | PerimeterNotFoundException | PrivilegeNotFoundException | UserNotFoundException | RightNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }
    }

}
