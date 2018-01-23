package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserRightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRightJpaRepository extends JpaRepository<UserRightEntity, Long> {

    boolean existsByUserLoginAndApplicationCode(String user, String application);

    UserRightEntity findByUserLoginAndApplicationCode(String user, String application);

    @Query("select u from UserRightEntity u " +
            "left join fetch u.details " +
            "where u.user.login = :user " +
            "and u.application.code = :application")
    UserRightEntity findByUserLoginAndApplicationCodeWithDetails(@Param("user") String user,
                                                                 @Param("application") String application);

    @Query("select distinct u from UserRightEntity u " +
            "left join fetch u.details " +
            "where u.user.id = :userId")
    List<UserRightEntity> findByUserIdWithDetails(@Param("userId") long userId);
}
