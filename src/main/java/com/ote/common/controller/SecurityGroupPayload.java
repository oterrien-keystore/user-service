package com.ote.common.controller;

import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SecurityGroupPayload implements IPayload {

    private long id;

    private String code;

    @Override
    public SecurityGroupEntity convert() {
        SecurityGroupEntity entity = new SecurityGroupEntity();
        entity.setId(getId());
        entity.setCode(getCode());
        return entity;
    }
}