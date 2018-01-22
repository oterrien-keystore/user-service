package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.SecurityGroupRightDetailEntity;
import com.ote.common.persistence.model.UserRightDetailEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRightDetailJpaRepository extends IEntityRepository<UserRightDetailEntity> {


    @Query("select case when count(ur) > 0 then true else false end " +
            "from UserRightDetailEntity ur " +
            "where ur.userRight.user.login = :user " +
            "and ur.userRight.application.code = :application " +
            "and ur.perimeter.code = :perimeter")
    boolean existsByUserLoginAndApplicationCodeAndPerimeterCode(@Param("user") String user,
                                                                @Param("application") String application,
                                                                @Param("perimeter") String perimeter);

    @Query("select ur from UserRightDetailEntity ur " +
            "where ur.userRight.user.login = :user " +
            "and ur.userRight.application.code = :application " +
            "and ur.perimeter.code = :perimeter")
    UserRightDetailEntity findByUserLoginAndApplicationCodeAndPerimeterCode(@Param("user") String user,
                                                                                     @Param("application") String application,
                                                                                     @Param("perimeter") String perimeter);
}
