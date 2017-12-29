package com.ote.common.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(of = "code")
@Table(name = "T_PRIVILEGE", uniqueConstraints = @UniqueConstraint(name="T_PRIVILEGE_AK", columnNames = "CODE"))
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;
}
