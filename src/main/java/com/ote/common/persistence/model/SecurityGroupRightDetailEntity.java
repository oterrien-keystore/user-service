package com.ote.common.persistence.model;

import com.ote.common.controller.UserRightDetailPayload;
import com.ote.crud.model.IEntity;
import com.ote.crud.model.IPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(of = {"securityGroupRight", "perimeter", "privileges"})
@Table(name = "T_SECURITY_GROUP_RIGHT_PERIMETER",
        uniqueConstraints = @UniqueConstraint(name = "AK_SECURITY_GROUP_RIGHT_PERIMETER", columnNames = {"SECURITY_GROUP_RIGHT_ID", "PERIMETER_ID"}))
public class SecurityGroupRightDetailEntity implements Serializable, IRightDetail, IEntity {

    private static final long serialVersionUID = 1L;

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
    @JoinTable(name = "T_SECURITY_GROUP_RIGHT_PERIMETER_PRIVILEGE",
            joinColumns = @JoinColumn(name = "SECURITY_GROUP_RIGHT_PERIMETER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"))
    private Set<PrivilegeEntity> privileges;

    @Override
    public UserRightDetailPayload convert() {
        UserRightDetailPayload payload = new UserRightDetailPayload();
        payload.setId(getId());
        payload.setPerimeter(getPerimeter().getCode());
        payload.setPrivileges(getPrivileges().stream().map(PrivilegeEntity::getCode).collect(Collectors.toList()));
        return payload;
    }
}
