package com.ote.common.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ote.common.persistence.model.UserRightDetailEntity;
import com.ote.common.persistence.repository.IPerimeterJpaRepository;
import com.ote.common.persistence.repository.IPrivilegeJpaRepository;
import com.ote.common.service.ApplicationContextProvider;
import com.ote.crud.model.IPayload;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserRightDetailPayload implements IPayload {

    @JsonIgnore
    private long id;

    private String perimeter;
    private List<String> privileges = new ArrayList<>();

    @Override
    public UserRightDetailEntity convert() {
        IPerimeterJpaRepository perimeterJpaRepository = ApplicationContextProvider.getContext().getBean(IPerimeterJpaRepository.class);
        IPrivilegeJpaRepository privilegeJpaRepository = ApplicationContextProvider.getContext().getBean(IPrivilegeJpaRepository.class);

        UserRightDetailEntity entity = new UserRightDetailEntity();
        entity.setId(id);
        entity.setPerimeter(perimeterJpaRepository.findByCode(getPerimeter()));
        entity.setPrivileges(privileges.stream().map(p -> privilegeJpaRepository.findByCode(p)).collect(Collectors.toSet()));
        return entity;
    }
}
