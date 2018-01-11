package com.ote.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ote.common.controller.PerimeterPayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"code", "perimeters", "privileges"})
@Table(name = "T_PERIMETER", uniqueConstraints = @UniqueConstraint(name = "T_PERIMETER_AK", columnNames = "CODE"))
public class PerimeterEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private PerimeterEntity parent;

    @OneToMany(mappedBy = "parent")
    private List<PerimeterEntity> perimeters;

    @ManyToMany
    @JoinTable(name = "T_PERIMETER_PRIVILEGE",
            joinColumns = @JoinColumn(name = "PERIMETER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"))
    private List<PrivilegeEntity> privileges = new ArrayList<>();

    @Override
    public PerimeterPayload convert() {
        PerimeterPayload payload = new PerimeterPayload();
        payload.setId(getId());
        payload.setCode(getCode());
        return payload;
    }
}
