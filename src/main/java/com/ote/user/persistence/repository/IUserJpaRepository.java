package com.ote.user.persistence.repository;

import com.ote.user.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);

    //UserDetails getUserDetails(String userName);
}
