package com.ote.rights.controller;

import com.ote.user.rights.api.IUserRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rights")
@Slf4j
public class UserRightCheckRestController {

    @Autowired
    private IUserRightService userRightService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(@RequestParam("user") String user,
                                                                 @RequestParam("application") String application,
                                                                 @RequestParam("perimeter") String perimeter,
                                                                 @RequestParam("privilege") String privilege) throws Exception {
        return userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
    }
}
