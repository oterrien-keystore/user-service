package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJpaRepository extends IEntityRepository<UserEntity> {

    boolean existsByLogin(String login);

    UserEntity findByLogin(String login);
}
