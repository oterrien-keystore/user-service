package com.ote.common.payload;

import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPayload implements IPayload {

    private long id;

    private String login;

    private String password;

    @Override
    public UserEntity convert() {
        UserEntity  entity = new UserEntity ();
        entity.setId(id);
        entity.setLogin(login);
        entity.setPassword(password);
        return entity;
    }
}
