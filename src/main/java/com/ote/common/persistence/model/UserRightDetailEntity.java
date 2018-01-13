package com.ote.common.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString(of = {"userRight", "perimeter", "privilege"})
@Table(name = "USER_RIGHT_DETAILS")
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

    @OneToOne
    private PrivilegeEntity privilege;
}
