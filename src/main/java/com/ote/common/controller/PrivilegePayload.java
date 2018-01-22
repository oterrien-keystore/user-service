package com.ote.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.common.persistence.repository.IPrivilegeJpaRepository;
import com.ote.common.service.ApplicationContextProvider;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class PrivilegePayload implements IPayload {

    private long id;

    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parent;

    @Override
    public PrivilegeEntity convert() {
        IPrivilegeJpaRepository privilegeJpaRepository = ApplicationContextProvider.getContext().getBean(IPrivilegeJpaRepository.class);
        PrivilegeEntity entity = new PrivilegeEntity();
        entity.setId(getId());
        entity.setCode(getCode());
        entity.setParent(Optional.ofNullable(parent).map(p -> privilegeJpaRepository.findByCode(p)).orElse(null));
        return entity;
    }
}
