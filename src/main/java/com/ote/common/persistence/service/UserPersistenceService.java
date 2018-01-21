package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.UserPayload;
import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.exception.CreateException;
import com.ote.crud.exception.MergeException;
import com.ote.crud.exception.ResetException;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistenceService extends AdtPersistenceService<UserPayload, UserEntity> {

    @Autowired
    private IEncryptorService encryptorService;

    @Getter
    private String scope = Scope.User.name();

    /**
     * create
     * Enforce password encryption
     *
     * @param payload
     * @return
     * @throws CreateException
     */
    @Override
    public UserPayload create(UserPayload payload) throws CreateException {
        try {
            payload.setPassword(encryptorService.encrypt(payload.getPassword()));
            return super.create(payload);
        } catch (EncryptingException e) {
            throw new CreateException(scope, e.getMessage(), e);
        }
    }

    /**
     * reset
     * Enforce password encryption
     *
     * @param payload
     * @return
     * @throws CreateException
     */
    @Override
    public Optional<UserPayload> reset(long id, UserPayload payload) throws ResetException {
        try {
            payload.setPassword(encryptorService.encrypt(payload.getPassword()));
            return super.reset(id, payload);
        } catch (EncryptingException e) {
            throw new ResetException(scope, id, e.getMessage(), e);
        }
    }

    /**
     * merge
     * Enforce password encryption
     *
     * @param payload
     * @return
     * @throws CreateException
     */
    @Override
    public Optional<UserPayload> merge(long id, UserPayload payload) throws MergeException {
        try {
            payload.setPassword(encryptorService.encrypt(payload.getPassword()));
            return super.merge(id, payload);
        } catch (EncryptingException e) {
            throw new MergeException(scope, id, e.getMessage(), e);
        }
    }
}
