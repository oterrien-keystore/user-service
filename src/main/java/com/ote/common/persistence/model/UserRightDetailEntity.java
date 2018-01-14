package com.ote.common.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = {"userRight", "perimeter", "privileges"})
@Table(name = "USER_RIGHTS_PERIMETERS")
public class UserRightDetailEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_RIGHT_ID")
    private UserRightEntity userRight;

    @OneToOne
    private PerimeterEntity perimeter;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_RIGHTS_PRIVILEGES_BY_PERIMETER",
            joinColumns = @JoinColumn(name = "USER_RIGHTS_PERIMETERS_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"))
    private Set<PrivilegeEntity> privileges;
}
