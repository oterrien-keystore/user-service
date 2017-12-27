package com.ote.user.persistence.repository;

import com.ote.user.persistence.model.ApplicationEntity;
import com.ote.user.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationJpaRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findByCode(String code);



    //UserDetails getUserDetails(String userName);
}
