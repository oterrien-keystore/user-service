package com.ote.common.persistence.model;

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
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;

    @OneToMany(mappedBy = "application")
    private List<UserRightEntity> userRights;
}
