package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ISecurityGroupJpaRepository extends IEntityRepository<SecurityGroupEntity> {

    boolean existsByCode(String code);

    SecurityGroupEntity findByCode(String code);

    @Query("select s from SecurityGroupEntity s join fetch s.securityGroupRights where s.code = :code")
    SecurityGroupEntity getByCode(@Param("code") String code);
}
