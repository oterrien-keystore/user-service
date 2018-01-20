package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupRightDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ISecurityGroupRightDetailJpaRepository extends JpaRepository<SecurityGroupRightDetailEntity, Long> {

    @Query("select case when count(sgrd) > 0 then true else false end " +
            "from SecurityGroupRightDetailEntity sgrd " +
            "where sgrd.securityGroupRight.securityGroup.code = :securityGroup " +
            "and sgrd.securityGroupRight.application.code = :application " +
            "and sgrd.perimeter.code = :perimeter")
    boolean existsBySecurityGroupCodeAndApplicationCodeAndPerimeterCode(@Param("securityGroup") String securityGroup,
                                                                        @Param("application") String application,
                                                                        @Param("perimeter") String perimeter);

    @Query("select sgrd from SecurityGroupRightDetailEntity sgrd " +
            "where sgrd.securityGroupRight.securityGroup.code = :securityGroup " +
            "and sgrd.securityGroupRight.application.code = :application " +
            "and sgrd.perimeter.code = :perimeter")
    SecurityGroupRightDetailEntity findBySecurityGroupCodeAndApplicationCodeAndPerimeterCode(@Param("securityGroup") String securityGroup,
                                                                                             @Param("application") String application,
                                                                                             @Param("perimeter") String perimeter);
}
