package com.ote.common.persistence.model;

import com.ote.common.controller.UserPayload;
import com.ote.crud.model.IEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = "login")
@EqualsAndHashCode(of = "login")
@Table(name = "T_USER", uniqueConstraints = @UniqueConstraint(name = "AK_USER", columnNames = "LOGIN"))
public class UserEntity implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "PASSWORD", nullable = false, length = 128)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<UserRightEntity> userRights;

    @ManyToMany(mappedBy = "users")
    private Set<SecurityGroupEntity> securityGroups;

    @Override
    public UserPayload convert() {
        UserPayload payload = new UserPayload();
        payload.setId(getId());
        payload.setLogin(getLogin());
        payload.setPassword(getPassword());
        return payload;
    }
}
