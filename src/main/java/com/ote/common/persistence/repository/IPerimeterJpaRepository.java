package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.PerimeterEntity;
import com.ote.crud.IEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPerimeterJpaRepository extends IEntityRepository<PerimeterEntity> {

}
