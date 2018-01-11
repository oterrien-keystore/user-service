package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationJpaRepository extends IEntityRepository<ApplicationEntity> {

    ApplicationEntity findByCode(String code);


    //UserDetails getUserDetails(String userName);
}
