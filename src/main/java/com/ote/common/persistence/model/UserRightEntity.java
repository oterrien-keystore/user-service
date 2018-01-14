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
@Table(name = "USER_RIGHTS", uniqueConstraints = @UniqueConstraint(name = "USER_RIGHTS_AK", columnNames = {"USER_ID", "APPLICATION_ID"}))
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
