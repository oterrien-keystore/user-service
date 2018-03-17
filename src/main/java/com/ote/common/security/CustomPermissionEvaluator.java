package com.ote.common.security;

import com.ote.user.rights.api.IRightCheckerService;
import com.ote.user.rights.api.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final IRightCheckerService userRightService;

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
            return userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(auth.getName(), applicationName, perimeter, permission);
        } catch (ApplicationNotFoundException | PerimeterNotFoundException | UserNotFoundException | PrivilegeNotFoundException | RightNotFoundException e) {
            log.info(e.getMessage(), e);
            return false;
        }
    }
}