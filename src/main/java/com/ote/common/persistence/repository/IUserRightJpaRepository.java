package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserRightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRightJpaRepository extends JpaRepository<UserRightEntity, Long> {

    @Query("select count(*) from UserRightEntity c where c.user.login = :user and c.application.code = :application")
    long countByUserAndApplication(@Param("user") String user,
                                       @Param("application") String application);

    @Query("select c from UserRightEntity c where c.user.login = :user and c.application.code = :application")
    List<UserRightEntity> findByUserAndApplication(@Param("user") String user,
                                                   @Param("application") String application);

    //List<Perimeter> getPerimeters(String user, String application);
}
