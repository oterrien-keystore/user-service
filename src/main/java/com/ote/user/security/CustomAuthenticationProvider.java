package com.ote.user.security;

import com.ote.user.service.UserCredentialServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Collections;

@Service
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