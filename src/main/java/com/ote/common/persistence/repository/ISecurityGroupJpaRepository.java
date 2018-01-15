package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface ISecurityGroupJpaRepository extends IEntityRepository<SecurityGroupEntity> {

    boolean existsByCode(String code);

    SecurityGroupEntity findByCode(String code);

    @EntityGraph(value = "securityGroupWithGroupRightsAndDetails")
    SecurityGroupEntity getByCode(String code);
}
