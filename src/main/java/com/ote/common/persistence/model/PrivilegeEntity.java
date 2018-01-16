package com.ote.common.persistence.model;

import com.ote.common.controller.PrivilegePayload;
import com.ote.crud.model.IEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString(of = "code")
@EqualsAndHashCode(of = "code")
@Table(name = "T_PRIVILEGE", uniqueConstraints = @UniqueConstraint(name = "AK_PRIVILEGE", columnNames = "CODE"))
public class PrivilegeEntity implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Override
    public PrivilegePayload convert() {
        PrivilegePayload payload = new PrivilegePayload();
        payload.setId(getId());
        payload.setCode(getCode());
        return payload;
    }
}
