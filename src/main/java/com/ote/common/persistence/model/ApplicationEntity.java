package com.ote.common.persistence.model;

import com.ote.common.controller.ApplicationPayload;
import com.ote.crud.model.IEntity;
import com.ote.crud.model.IPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = "code")
@Table(name = "T_APPLICATION", uniqueConstraints = @UniqueConstraint(name="T_APPLICATION_AK", columnNames = "CODE"))
public class ApplicationEntity implements IEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;

    @OneToMany(mappedBy = "application")
    private List<UserRightEntity> userRights;

    @Override
    public ApplicationPayload convert() {
        ApplicationPayload payload = new ApplicationPayload();
        payload.setId(getId());
        payload.setCode(getCode());
        return payload;
    }
}
