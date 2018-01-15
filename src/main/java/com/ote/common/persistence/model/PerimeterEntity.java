package com.ote.common.persistence.model;

import com.ote.common.controller.PerimeterPayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = {"code"})
@Table(name = "T_PERIMETER", uniqueConstraints = @UniqueConstraint(name = "AK_PERIMETER", columnNames = "CODE"))
public class PerimeterEntity implements IEntity, Serializable {

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
    private Set<PerimeterEntity> perimeters;

    @Override
    public PerimeterPayload convert() {
        PerimeterPayload payload = new PerimeterPayload();
        payload.setId(getId());
        payload.setCode(getCode());
        return payload;
    }
}
