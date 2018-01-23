package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationJpaRepository extends IEntityRepository<ApplicationEntity> {

    boolean existsByCode(String code);

    ApplicationEntity findByCode(String code);
}
