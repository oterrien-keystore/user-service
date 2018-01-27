package com.ote.common.exceptionhandler;

import com.ote.common.Error;
import com.ote.user.rights.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserServiceExceptionHandler {

    @ExceptionHandler(ApplicationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(ApplicationNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(UserNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(PerimeterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(PerimeterNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(PrivilegeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(PrivilegeNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(RoleNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }
}
