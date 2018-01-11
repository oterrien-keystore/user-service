package com.ote.common.persistence.model;

import com.ote.common.controller.PrivilegePayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(of = "code")
@Table(name = "T_PRIVILEGE", uniqueConstraints = @UniqueConstraint(name = "T_PRIVILEGE_AK", columnNames = "CODE"))
public class PrivilegeEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;

    @Override
    public PrivilegePayload convert() {
        PrivilegePayload payload = new PrivilegePayload();
        payload.setId(getId());
        payload.setCode(getCode());
        return payload;
    }
}
