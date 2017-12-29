package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);
}
