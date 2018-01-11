package com.ote.common.controller;

import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRightPayload implements IPayload {

    @Getter
    public static final String EntityName = "Application";

    private long id;

    private String code;

    @Override
    public ApplicationEntity convert() {
        ApplicationEntity entity = new ApplicationEntity();
        entity.setId(getId());
        entity.setCode(getCode());
        return entity;
    }
}
