package com.ote.user.persistence;

import com.ote.common.persistence.model.UserRightDetailEntity;
import com.ote.common.persistence.model.UserRightEntity;
import com.ote.common.persistence.repository.IApplicationJpaRepository;
import com.ote.common.persistence.repository.IUserJpaRepository;
import com.ote.common.persistence.repository.IUserRightJpaRepository;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.spi.IUserRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRightPersistenceServiceAdapter implements IUserRightRepository {

    @Autowired
    private IUserRightJpaRepository userRightJpaRepository;

    @Autowired
    private IUserJpaRepository userJpaRepository;

    @Autowired
    private IApplicationJpaRepository applicationJpaRepository;

    @Override
    public boolean isUserDefined(String user) {
        return userJpaRepository.existsByLogin(user);
    }

    @Override
    public boolean isApplicationDefined(String application) {
        return applicationJpaRepository.existsByCode(application);
    }

    @Override
    public boolean isRoleDefined(String user, String application) {
        return userRightJpaRepository.existsByUserLoginAndApplicationCode(user, application);
    }

    @Override
    public List<Perimeter> getPerimeters(String user, String application) {

        UserRightEntity userRightEntity = userRightJpaRepository.getByUserLoginAndApplicationCode(user, application);

        Map<String, Perimeter> perimeters = groupByCode(userRightEntity.getDetails());
        setParents(userRightEntity.getDetails(), perimeters);
        setPrivileges(userRightEntity.getDetails(), perimeters);

        return new ArrayList<>(perimeters.values());
    }

    private Map<String, Perimeter> groupByCode(Collection<UserRightDetailEntity> details) {
        Map<String, Perimeter> perimeters = new HashMap<>();
        details.stream().
                forEach(detailEntity -> {
                    String code = detailEntity.getPerimeter().getCode();
                    if (!perimeters.containsKey(code)) {
                        perimeters.put(code, new Perimeter(code));
                    }
                });

        return perimeters;
    }

    private void setParents(Collection<UserRightDetailEntity> details, Map<String, Perimeter> perimeters) {
        details.stream().
                filter(detailEntity -> Optional.ofNullable(detailEntity.getPerimeter().getParent()).isPresent()).
                filter(detailEntity -> perimeters.containsKey(detailEntity.getPerimeter().getParent().getCode())).
                forEach(detailEntity -> {
                    String code = detailEntity.getPerimeter().getCode();
                    String parent = detailEntity.getPerimeter().getParent().getCode();
                    Perimeter perimeter = perimeters.get(code);
                    Perimeter parentPerimeter = perimeters.get(parent);
                    parentPerimeter.getPerimeters().add(perimeter);
                });
    }

    private void setPrivileges(Collection<UserRightDetailEntity> details, Map<String, Perimeter> perimeters) {
        details.stream().
                forEach(detailEntity -> {
                    String code = detailEntity.getPerimeter().getCode();
                    Perimeter perimeter = perimeters.get(code);
                    perimeter.getPrivileges().add(detailEntity.getPrivilege().getCode());
                });
    }

}
