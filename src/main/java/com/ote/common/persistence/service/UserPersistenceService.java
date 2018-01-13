package com.ote.common.persistence.service;

import com.ote.common.controller.UserPayload;
import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.IEntityRepository;
import com.ote.crud.IPersistenceService;
import com.ote.crud.exception.CreateException;
import com.ote.crud.exception.MergeException;
import com.ote.crud.exception.ResetException;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistenceService implements IPersistenceService<UserPayload> {

    @Autowired
    @Getter
    private IEntityRepository<UserEntity> entityRepository;

    @Autowired
    private IEncryptorService encryptorService;

    @Getter
    private String entityName = "USER";

    @Value("${page.default.size}")
    @Getter
    private int defaultPageSize;

    @Override
    public UserPayload create(UserPayload payload) throws CreateException {
        try {
            payload.setPassword(encryptorService.encrypt(payload.getPassword()));
            return IPersistenceService.super.create(payload);
        } catch (EncryptingException e) {
            throw new CreateException(entityName, e);
        }
    }

    @Override
    public Optional<UserPayload> reset(long id, UserPayload payload) throws ResetException {
        try {
            payload.setPassword(encryptorService.encrypt(payload.getPassword()));
            return IPersistenceService.super.reset(id, payload);
        } catch (EncryptingException e) {
            throw new ResetException(entityName, id, e);
        }
    }

    @Override
    public Optional<UserPayload> merge(long id, UserPayload payload) throws MergeException {
        try {
            payload.setPassword(encryptorService.encrypt(payload.getPassword()));
            return IPersistenceService.super.merge(id, payload);
        } catch (EncryptingException e) {
            throw new MergeException(entityName, id, e);
        }
    }
}
