package com.ote.rights.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/securityGroups")
public class SecurityGroupRestController {

    @GetMapping(value = "/{id}/rights", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserRightOrganizedPayload getRights(@PathVariable("id") long id){
        return null;
    }

    @PutMapping(value = "/{id}/rights", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void addRights(@PathVariable("id") long id, @RequestBody UserRightPayload payload) {

    }

    @DeleteMapping(value = "/{id}/rights", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void removeRights(@PathVariable("id") long id, @RequestBody UserRightPayload payload) {

    }

    @PutMapping(value = "/{id}/users/{login}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void addUser(@PathVariable("id") long id, @PathVariable("login") String user) {

    }

    @DeleteMapping(value = "/{id}/users/{login}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteUser(@PathVariable("id") long id, @PathVariable("login") String user) {

    }
}
