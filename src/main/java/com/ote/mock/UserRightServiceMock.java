package com.ote.mock;

import com.ote.user.rights.api.IUserRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

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
}
