package com.ote.common.persistence.model;

import com.ote.common.controller.UserPayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = {"login"})
@Table(name = "USERS", uniqueConstraints = @UniqueConstraint(name = "USERS_AK", columnNames = "LOGIN"))
@NamedEntityGraph(name = "userWithUserRightsAndDetails",
        attributeNodes = @NamedAttributeNode(value = "userRights", subgraph = "userRightsAndDetails"),
        subgraphs = @NamedSubgraph(name = "userRightsAndDetails", attributeNodes = @NamedAttributeNode("details")))
public class UserEntity implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD", nullable = false, length = 128)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<UserRightEntity> userRights;

    @Override
    public UserPayload convert() {
        UserPayload payload = new UserPayload();
        payload.setId(id);
        payload.setLogin(login);
        payload.setPassword(password);
        return payload;
    }
}
