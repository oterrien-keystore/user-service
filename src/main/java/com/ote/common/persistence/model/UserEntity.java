package com.ote.common.persistence.model;

import com.ote.common.controller.UserPayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"login"})
@Table(name = "T_USER", uniqueConstraints = @UniqueConstraint(name = "T_USER_AK", columnNames = "LOGIN"))
public class UserEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD", nullable = false, length = 128)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<UserRightEntity> userRights;

    @Override
    public UserPayload convert() {
        UserPayload payload = new UserPayload();
        payload.setId(id);
        payload.setLogin(login);
        payload.setPassword(password);
        return payload;
    }
}
