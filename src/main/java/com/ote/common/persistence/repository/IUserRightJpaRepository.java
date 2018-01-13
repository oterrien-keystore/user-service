package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserRightEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRightJpaRepository extends JpaRepository<UserRightEntity, Long> {

    boolean existsByUserLoginAndApplicationCode(String user, String application);

    UserRightEntity findByUserLoginAndApplicationCode(String user, String application);

    @EntityGraph(value = "userRightWithDetails")
    UserRightEntity getByUserLoginAndApplicationCode(String user, String application);
}
