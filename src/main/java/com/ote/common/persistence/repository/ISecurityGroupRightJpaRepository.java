package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupRightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISecurityGroupRightJpaRepository extends JpaRepository<SecurityGroupRightEntity, Long> {

    boolean existsBySecurityGroupCodeAndApplicationCode(String securityGroup, String application);

    @Query("select distinct sgr from SecurityGroupRightEntity sgr " +
            "left join fetch sgr.details " +
            "where sgr.securityGroup.code = :securityGroup ")
    List<SecurityGroupRightEntity> findBySecurityGroupCodeWithDetails(@Param("securityGroup") String securityGroup);

    @Query("select distinct sgr from SecurityGroupRightEntity sgr " +
            "left join fetch sgr.details " +
            "where sgr.securityGroup.code = :securityGroup " +
            "and sgr.application.code = :application")
    SecurityGroupRightEntity findBySecurityGroupCodeAndApplicationCodeWithDetails(@Param("securityGroup") String securityGroup,
                                                                                  @Param("application") String application);

    @Query("select case when count(sgr) > 0 then true else false end " +
            "from SecurityGroupRightEntity sgr " +
            "inner join sgr.securityGroup sg " +
            "inner join sg.users u " +
            "where sgr.application.code = :application " +
            "and u.login = :user")
    boolean existsByUserLoginAndApplicationCode(@Param("user") String user,
                                                @Param("application") String application);

    @Query("select distinct sgr from SecurityGroupRightEntity sgr " +
            "left join fetch sgr.details " +
            "inner join fetch sgr.securityGroup sg " +
            "inner join sg.users u " +
            "where sgr.application.code = :application " +
            "and u.login = :user")
    List<SecurityGroupRightEntity> findByUserLoginAndApplicationCodeWithDetails(@Param("user") String user,
                                                                                @Param("application") String application);

    @Query("select distinct sgr from SecurityGroupRightEntity sgr " +
            "left join fetch sgr.details " +
            "inner join fetch sgr.securityGroup sg " +
            "inner join sg.users u " +
            "where u.id = :userId")
    List<SecurityGroupRightEntity> findByUserIdWithDetails(@Param("userId") long userId);
}