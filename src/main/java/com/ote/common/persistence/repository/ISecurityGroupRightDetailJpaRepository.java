package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupRightDetailEntity;
import com.ote.common.persistence.model.UserRightDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISecurityGroupRightDetailJpaRepository extends JpaRepository<SecurityGroupRightDetailEntity, Long> {

    List<SecurityGroupRightDetailEntity> findBySecurityGroupRightId(long id);
}
