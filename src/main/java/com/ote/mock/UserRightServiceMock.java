package com.ote.mock;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.exception.UserRightServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("mockUserRight")
@Service
@Slf4j
public class UserRightServiceMock implements IUserRightService {

    public UserRightServiceMock() {
        log.warn("###### MOCK ##### ");
    }

    @Override
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege) {
        return true;
    }

    @Override
    public void addRights(String user, String application, String perimeter, String privilege) throws UserRightServiceException {

    }
}
