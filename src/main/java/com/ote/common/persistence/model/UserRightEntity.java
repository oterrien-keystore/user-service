package com.ote.common.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"user", "application", "perimeters"})
@Table(name = "T_USER_RIGHT", uniqueConstraints = @UniqueConstraint(name="T_USER_RIGHT_AK", columnNames = {"USER_ID", "APPLICATION_ID"}))
public class UserRightEntity {

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

    @ManyToMany
    @JoinTable(name = "T_USER_RIGHT_PERIMETER",
            joinColumns = @JoinColumn(name = "USER_RIGHT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PERIMETER_ID", referencedColumnName = "ID"))
    private List<PerimeterEntity> perimeters;
}
