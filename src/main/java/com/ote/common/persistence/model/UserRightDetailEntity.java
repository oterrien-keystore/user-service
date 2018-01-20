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
@ToString(of = {"userRight", "perimeter", "privileges"})
@Table(name = "T_USER_RIGHT_PERIMETER", uniqueConstraints = @UniqueConstraint(name = "AK_USER_RIGHT_PERIMETER", columnNames = {"USER_RIGHT_ID", "PERIMETER_ID"}))
public class UserRightDetailEntity implements Serializable, IRightDetail {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_RIGHT_ID", nullable = false)
    private UserRightEntity userRight;

    @ManyToOne(optional = false)
    //@OneToOne(fetch = FetchType=LAZY)
    //@JoinColumn(name="PERIMETER_ID", nullable = false)
    private PerimeterEntity perimeter;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_USER_RIGHT_PERIMETER_PRIVILEGE",
            joinColumns = @JoinColumn(name = "USER_RIGHT_PERIMETER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"))
    private Set<PrivilegeEntity> privileges;
}
