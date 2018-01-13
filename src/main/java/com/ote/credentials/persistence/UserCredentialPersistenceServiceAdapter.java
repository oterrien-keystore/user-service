package com.ote.credentials.persistence;

import com.ote.common.persistence.repository.IUserJpaRepository;
import com.ote.user.credentials.spi.IUserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialPersistenceServiceAdapter implements IUserCredentialRepository {

    @Autowired
    private IUserJpaRepository userJpaRepository;

    @Override
    public boolean isUserDefined(String user) {
        return userJpaRepository.existsByLogin(user);
    }

    @Override
    public String getPassword(String user) {
        return userJpaRepository.findByLogin(user).getPassword();
    }
}
