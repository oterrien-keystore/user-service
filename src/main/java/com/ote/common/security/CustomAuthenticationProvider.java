package com.ote.common.security;

import com.ote.credentials.service.UserCredentialServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserCredentialServiceAdapter userCredentialServiceAdapter;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = auth.getName();
        String password = auth.getCredentials().toString();

        if (userCredentialServiceAdapter.areCredentialsCorrect(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        }
        throw new BadCredentialsException("Authentication failed for user " + username);
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}