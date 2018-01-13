package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserRightDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRightDetailJpaRepository extends JpaRepository<UserRightDetailEntity, Long> {

}
