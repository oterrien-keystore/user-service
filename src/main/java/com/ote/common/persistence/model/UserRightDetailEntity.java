package com.ote.common.persistence.model;

import com.ote.common.controller.UserRightDetailPayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(of = {"userRight", "perimeter", "privileges"})
@Table(name = "T_USER_RIGHT_PERIMETER", uniqueConstraints = @UniqueConstraint(name = "AK_USER_RIGHT_PERIMETER", columnNames = {"USER_RIGHT_ID", "PERIMETER_ID"}))
public class UserRightDetailEntity implements Serializable, IRightDetail, IEntity {

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

    @Override
    public UserRightDetailPayload convert() {
        UserRightDetailPayload payload = new UserRightDetailPayload();
        payload.setId(getId());
        payload.setPerimeter(getPerimeter().getCode());
        payload.setPrivileges(getPrivileges().stream().map(PrivilegeEntity::getCode).collect(Collectors.toList()));
        return payload;
    }
}
