package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJpaRepository extends IEntityRepository<UserEntity> {

    boolean existsByLogin(String login);

    UserEntity findByLogin(String login);

    @Query("select u from UserEntity u " +
            "left join fetch u.userRights ur " +
            "left join fetch ur.details urd " +
            "left join fetch u.securityGroups sg " +
            "left join fetch sg.securityGroupRights sgr " +
            "left join fetch sgr.details sgrd " +
            "where u.login = :login")
    UserEntity findByLoginWithUserRightsAndSecurityGroupRights(@Param("login") String login);
}
