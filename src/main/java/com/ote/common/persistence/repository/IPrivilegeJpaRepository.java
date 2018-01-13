package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPrivilegeJpaRepository extends IEntityRepository<PrivilegeEntity> {

    boolean existsByCode(String code);

    PrivilegeEntity findByCode(String code);
}
