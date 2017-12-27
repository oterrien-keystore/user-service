package com.ote.user.service;

import com.ote.user.credentials.api.IUserCredentialService;
import com.ote.user.credentials.api.UserCredentialServiceProvider;
import com.ote.user.credentials.api.exception.UserNotFoundException;
import com.ote.user.credentials.spi.IUserCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCredentialServiceAdapter implements IUserCredentialService {

    private final IUserCredentialService userCredentialService;

    public UserCredentialServiceAdapter(@Autowired IUserCredentialRepository userCredentialRepository) {
        this.userCredentialService = UserCredentialServiceProvider.getInstance().getFactory().createService(userCredentialRepository);
    }

    @Override
    public boolean areCredentialsCorrect(String user, String password) {
        try {
            return userCredentialService.areCredentialsCorrect(user, password);
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }
}
