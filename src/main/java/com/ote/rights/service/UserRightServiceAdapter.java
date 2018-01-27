package com.ote.rights.service;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.UserRightServiceProvider;
import com.ote.user.rights.api.exception.UserRightServiceException;
import com.ote.user.rights.spi.IUserRightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!mockUserRight")
@Service
@Slf4j
public class UserRightServiceAdapter implements IUserRightService {

    private final IUserRightService userRightService;

    public UserRightServiceAdapter(@Autowired IUserRightRepository userRightRepository) {
        this.userRightService = UserRightServiceProvider.getInstance().getFactory().createService(userRightRepository);
    }

    @Override
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege) throws UserRightServiceException {
        return userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
    }

    @Override
    public void addRights(String user, String application, String perimeter, String privilege) {
        try {
            userRightService.addRights(user, application, perimeter, privilege);
        } catch (UserRightServiceException e) {
            log.info(e.getMessage(), e);
        }
    }
}
