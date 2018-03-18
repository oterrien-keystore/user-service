package com.ote.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserPayload implements IPayload {

    private long id;

    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Override
    public UserEntity convert() {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setLogin(login);
        entity.setPassword(password);
        return entity;
    }
}
