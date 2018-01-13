package com.ote.credentials.controller;

import com.ote.user.credentials.api.IUserCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credentials")
public class UserCredentialCheckRestController {

    @Autowired
    private IUserCredentialService userCredentialService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean checkCredentials(@RequestParam("user") String user,
                                    @RequestParam("password") String password) throws Exception {

        return userCredentialService.areCredentialsCorrect(user, password);
    }
}
