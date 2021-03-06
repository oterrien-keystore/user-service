package com.ote.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.common.persistence.repository.IUserJpaRepository;
import com.ote.common.service.ApplicationContextProvider;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SecurityGroupPayload implements IPayload {

    private long id;

    private String code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<RightPayload> rights = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<UserPayload> users = new ArrayList<>();

    @Override
    public SecurityGroupEntity convert() {
        SecurityGroupEntity entity = new SecurityGroupEntity();
        entity.setId(getId());
        entity.setCode(getCode());
        entity.setUsers(entity.getUsers());
        return entity;
    }
}