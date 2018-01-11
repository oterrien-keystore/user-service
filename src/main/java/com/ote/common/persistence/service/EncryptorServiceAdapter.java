package com.ote.common.persistence.service;

import com.ote.user.credentials.api.EncryptorServiceProvider;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.exception.EncryptingException;
import org.springframework.stereotype.Service;

@Service
public class EncryptorServiceAdapter implements IEncryptorService {

    private final IEncryptorService service;

    public EncryptorServiceAdapter() {
        this.service = EncryptorServiceProvider.getInstance().getFactory().createService();
    }

    @Override
    public String encrypt(String value) throws EncryptingException {
        return service.encrypt(value);
    }
}
