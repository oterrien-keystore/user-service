package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJpaRepository extends IEntityRepository<UserEntity> {

    boolean existsByLogin(String login);

    UserEntity findByLogin(String login);

    @EntityGraph(value = "userWithUserRightsAndDetails")
    ApplicationEntity getByLogin(String code);
}
