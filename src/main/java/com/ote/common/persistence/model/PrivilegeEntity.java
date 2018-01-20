package com.ote.common.persistence.model;

import com.ote.common.controller.PrivilegePayload;
import com.ote.crud.model.IEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Getter
@Setter
@ToString(of = "code")
@EqualsAndHashCode(of = "code")
@Table(name = "T_PRIVILEGE", uniqueConstraints = @UniqueConstraint(name = "AK_PRIVILEGE", columnNames = {"CODE", "PARENT_ID"}))
public class PrivilegeEntity implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private PrivilegeEntity parent;

    @Override
    public PrivilegePayload convert() {
        PrivilegePayload payload = new PrivilegePayload();
        payload.setId(getId());
        payload.setCode(getCode());
        payload.setParent(Optional.ofNullable(getParent()).map(p -> p.getCode()).orElse(null));
        return payload;
    }
}
