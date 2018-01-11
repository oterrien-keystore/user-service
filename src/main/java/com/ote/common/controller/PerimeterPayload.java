package com.ote.common.controller;

import com.ote.common.persistence.model.PerimeterEntity;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PerimeterPayload implements IPayload {

    @Getter
    public static final String EntityName = "Application";

    private long id;

    private String code;

    @Override
    public PerimeterEntity convert() {
        PerimeterEntity entity = new PerimeterEntity();
        entity.setId(getId());
        entity.setCode(getCode());
        return entity;
    }
}
