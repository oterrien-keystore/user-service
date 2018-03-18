package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISecurityGroupJpaRepository extends IEntityRepository<SecurityGroupEntity> {

    boolean existsByCode(String code);

    SecurityGroupEntity findByCode(String code);

    @Query("select s from SecurityGroupEntity s " +
            "left join fetch s.securityGroupRights sgr " +
            "left join fetch sgr.details sgrd " +
            "where s.code = :code")
    SecurityGroupEntity getByCode(@Param("code") String code);

    @Query("select s from SecurityGroupEntity s " +
            "inner join fetch s.users u " +
            "where u.login = :user")
    List<SecurityGroupEntity> findByUser(@Param("user") String user);
}