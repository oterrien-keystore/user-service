package com.ote.common.controller;

import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrivilegePayload implements IPayload {

    @Getter
    public static final String EntityName = "Application";

    private long id;

    private String code;

    @Override
    public PrivilegeEntity convert() {
        PrivilegeEntity entity = new PrivilegeEntity();
        entity.setId(getId());
        entity.setCode(getCode());
        return entity;
    }
}
