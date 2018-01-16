package com.ote.common.persistence.model;

import com.ote.crud.model.IEntity;
import com.ote.crud.model.IPayload;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = {"securityGroup", "application"})
@EqualsAndHashCode(of = {"securityGroup", "application"})
@Table(name = "T_SECURITY_GROUP_RIGHT", uniqueConstraints = @UniqueConstraint(name = "AK_SECURITY_GROUP_RIGHT", columnNames = {"SECURITY_GROUP_ID", "APPLICATION_ID"}))
public class SecurityGroupRightEntity implements IEntity {

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

    @Override
    public <TP extends IPayload> TP convert() {
        return null;
    }
}
