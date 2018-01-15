package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupRightEntity;
import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISecurityGroupRightJpaRepository extends IEntityRepository<SecurityGroupRightEntity> {

    boolean existsBySecurityGroupCodeAndApplicationCode(String securityGroup, String application);

    SecurityGroupRightEntity findBySecurityGroupCodeAndApplicationCode(String securityGroup, String application);

    @Query("select sgr from SecurityGroupRightEntity sgr " +
            "join fetch sgr.details " +
            "where sgr.application.code = :application " +
            "and :user member of sgr.securityGroup.users")
    List<SecurityGroupRightEntity> findByUserAndApplicationCode(@Param("user") UserEntity user,
                                                                @Param("application") String application);
}
