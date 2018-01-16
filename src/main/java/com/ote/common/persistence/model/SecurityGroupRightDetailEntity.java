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
@ToString(of = {"securityGroupRight", "perimeter", "privileges"})
@Table(name = "T_SECURITY_GROUP_RIGHT_PERIMETER", uniqueConstraints = @UniqueConstraint(name = "AK_SECURITY_GROUP_RIGHT_PERIMETER", columnNames = {"SECURITY_GROUP_RIGHT_ID", "PERIMETER_ID"}))
public class SecurityGroupRightDetailEntity implements Serializable, IRightDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "SECURITY_GROUP_RIGHT_ID", nullable = false)
    private SecurityGroupRightEntity securityGroupRight;

    @OneToOne
    private PerimeterEntity perimeter;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_SECURITY_GROUP_RIGHT_PRIVILEGE",
            joinColumns = @JoinColumn(name = "SECURITY_GROUP_RIGHT_PERIMETER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"))
    private Set<PrivilegeEntity> privileges;
}
