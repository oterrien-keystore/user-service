package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.PerimeterEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPerimeterJpaRepository extends IEntityRepository<PerimeterEntity> {

    boolean existsByCode(String code);

    PerimeterEntity findByCode(String code);

}
