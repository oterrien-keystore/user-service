package com.ote.rights.service;

import com.ote.user.rights.api.IRightCheckerService;
import com.ote.user.rights.api.RightServiceProvider;
import com.ote.user.rights.api.exception.*;
import com.ote.user.rights.spi.IRightCheckerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserRightServiceAdapter implements IRightCheckerService {

    private final IRightCheckerService userRightService;

    public UserRightServiceAdapter(@Autowired IRightCheckerRepository userRightRepository) {
        this.userRightService = RightServiceProvider.getInstance().getFactory().createService(userRightRepository);
    }

    @Override
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws ApplicationNotFoundException, PerimeterNotFoundException, PrivilegeNotFoundException, UserNotFoundException, RightNotFoundException {
        return userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
    }

}
