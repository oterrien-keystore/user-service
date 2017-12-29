package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationJpaRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findByCode(String code);



    //UserDetails getUserDetails(String userName);
}
