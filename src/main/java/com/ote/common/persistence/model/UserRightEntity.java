package com.ote.common.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = {"user", "application"})
@Table(name = "T_USER_RIGHT", uniqueConstraints = @UniqueConstraint(name = "AK_USER_RIGHT", columnNames = {"USER_ID", "APPLICATION_ID"}))
@NamedEntityGraph(name = "userRightWithDetails", attributeNodes = @NamedAttributeNode(value = "details"))
public class UserRightEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID")
    private ApplicationEntity application;

    @OneToMany(mappedBy = "userRight")
    private Set<UserRightDetailEntity> details;
}
