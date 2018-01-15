package com.ote.common.persistence.model;

import com.ote.crud.model.IEntity;
import com.ote.crud.model.IPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = "code")
@Table(name = "T_SECURITY_GROUP", uniqueConstraints = @UniqueConstraint(name = "AK_SECURITY_GROUP", columnNames = "CODE"))
@NamedEntityGraph(name = "securityGroupWithGroupRightsAndDetails",
        attributeNodes = @NamedAttributeNode(value = "securityGroupRights", subgraph = "securityGroupRightsAndDetails"),
        subgraphs = @NamedSubgraph(name = "securityGroupRightsAndDetails", attributeNodes = @NamedAttributeNode("details")))
public class SecurityGroupEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_SECURITY_GROUP_USER",
            joinColumns = @JoinColumn(name = "SECURITY_GROUP_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "securityGroup")
    private Set<SecurityGroupRightEntity> securityGroupRights;

    @Override
    public <TP extends IPayload> TP convert() {
        return null;
    }
}
