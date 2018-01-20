package com.ote.common.persistence.repository;

import com.ote.common.persistence.model.PerimeterEntity;
import com.ote.crud.IEntityRepository;
import com.ote.user.rights.api.Path;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicReference;

@Repository
public interface IPerimeterJpaRepository extends IEntityRepository<PerimeterEntity> {

    boolean existsByParentCodeAndCode(String parent, String code);

    PerimeterEntity findByParentCodeAndCode(String parent, String code);

    default boolean existsByCode(String code) {
        return existsByPath(new Path.Parser(code).get());
    }

    default boolean existsByPath(Path path) {
        AtomicReference<String> parent = new AtomicReference<>();
        return path.stream().
                allMatch(p -> {
                    boolean result = existsByParentCodeAndCode(parent.get(), p);
                    parent.set(p);
                    return result;
                });
    }

    default PerimeterEntity findByCode(String code) {
        return findByPath(new Path.Parser(code).get());
    }

    default PerimeterEntity findByPath(Path path) {
        AtomicReference<String> parent = new AtomicReference<>();
        AtomicReference<PerimeterEntity> entity = new AtomicReference<>();
        path.stream().
                forEach(p -> {
                    entity.set(findByParentCodeAndCode(parent.get(), p));
                    parent.set(p);
                });
        return entity.get();
    }

}
