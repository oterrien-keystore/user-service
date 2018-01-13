package com.ote.common.persistence.model;

import com.ote.common.controller.ApplicationPayload;
import com.ote.crud.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = "code")
@Table(name = "APPLICATIONS", uniqueConstraints = @UniqueConstraint(name = "APPLICATIONS_AK", columnNames = "CODE"))
@NamedEntityGraph(name = "applicationWithUserRightsAndDetails",
        attributeNodes = @NamedAttributeNode(value = "userRights", subgraph = "userRightsAndDetails"),
        subgraphs = @NamedSubgraph(name = "userRightsAndDetails", attributeNodes = @NamedAttributeNode("details")))
public class ApplicationEntity implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE")
    private String code;

    @OneToMany(mappedBy = "application")
    private Set<UserRightEntity> userRights;

    @Override
    public ApplicationPayload convert() {
        ApplicationPayload payload = new ApplicationPayload();
        payload.setId(getId());
        payload.setCode(getCode());
        return payload;
    }
}
