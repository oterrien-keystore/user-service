package com.ote.common.persistence.model;

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
@ToString(of = {"user", "application"})
@EqualsAndHashCode(of = {"user", "application"})
@Table(name = "T_USER_RIGHT", uniqueConstraints = @UniqueConstraint(name = "AK_USER_RIGHT", columnNames = {"USER_ID", "APPLICATION_ID"}))
public class UserRightEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID", nullable = false)
    private ApplicationEntity application;

    @OneToMany(mappedBy = "userRight")
    private Set<UserRightDetailEntity> details;
}
