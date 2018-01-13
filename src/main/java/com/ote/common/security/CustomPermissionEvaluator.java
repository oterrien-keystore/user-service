package com.ote.common.security;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.PerimeterPath;
import com.ote.user.rights.api.exception.ApplicationNotFoundException;
import com.ote.user.rights.api.exception.RoleNotFoundException;
import com.ote.user.rights.api.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final IUserRightService userRightService;

    private final String applicationName;

    @Override
    public boolean hasPermission(Authentication auth, Object perimeter, Object permission) {
        return hasPrivilege(auth, perimeter.toString(), permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String perimeter, Object permission) {
        return hasPrivilege(auth, perimeter, permission.toString());
    }

    private boolean hasPrivilege(Authentication auth, String perimeter, String permission) {
        try {
            PerimeterPath perimeterPath = new PerimeterPath.Parser(perimeter).get();
            return userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(auth.getName(), applicationName, perimeterPath, permission);
        } catch (UserNotFoundException | ApplicationNotFoundException | RoleNotFoundException e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }
}