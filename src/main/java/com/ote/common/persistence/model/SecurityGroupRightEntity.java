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
@ToString(of = {"securityGroup", "application"})
@EqualsAndHashCode(of = {"securityGroup", "application"})
@Table(name = "T_SECURITY_GROUP_RIGHT", uniqueConstraints = @UniqueConstraint(name = "AK_SECURITY_GROUP_RIGHT", columnNames = {"SECURITY_GROUP_ID", "APPLICATION_ID"}))
public class SecurityGroupRightEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "SECURITY_GROUP_ID", nullable = false)
    private SecurityGroupEntity securityGroup;

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID", nullable = false)
    private ApplicationEntity application;

    @OneToMany(mappedBy = "securityGroupRight")
    private Set<SecurityGroupRightDetailEntity> details;
}