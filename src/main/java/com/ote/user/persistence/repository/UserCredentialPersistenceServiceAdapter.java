package com.ote.user.persistence.repository;

import com.ote.user.credentials.spi.IUserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCredentialPersistenceServiceAdapter implements IUserCredentialRepository {

    @Autowired
    private IUserJpaRepository userJpaRepository;

    @Override
    public boolean isUserDefined(String user) {
        return Optional.ofNullable(userJpaRepository.findByLogin(user)).isPresent();
    }

    @Override
    public String getPassword(String user) {
        return userJpaRepository.findByLogin(user).getPassword();
    }
}
