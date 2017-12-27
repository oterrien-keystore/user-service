package com.ote.user.persistence.repository;

import com.ote.user.persistence.model.PerimeterEntity;
import com.ote.user.persistence.model.UserRightEntity;
import com.ote.user.persistence.repository.IApplicationJpaRepository;
import com.ote.user.persistence.repository.IUserJpaRepository;
import com.ote.user.persistence.repository.IUserRightJpaRepository;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.spi.IUserRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return Optional.ofNullable(userJpaRepository.findByLogin(user)).isPresent();
    }

    @Override
    public boolean isApplicationDefined(String application) {
        return Optional.ofNullable(applicationJpaRepository.findByCode(application)).isPresent();
    }

    @Override
    public boolean isRoleDefined(String user, String application) {
        return userRightJpaRepository.countByUserAndApplication(user, application) > 0;
    }

    @Override
    public List<Perimeter> getPerimeters(String user, String application) {

        List<UserRightEntity> userRights = userRightJpaRepository.findByUserAndApplication(user, application);

        return userRights.stream().
                flatMap(p -> p.getPerimeters().stream()).
                map(perimeterEntity -> convert(perimeterEntity)).
                collect(Collectors.toList());
    }

    private Perimeter convert(PerimeterEntity perimeterEntity) {
        Perimeter perimeter = new Perimeter(perimeterEntity.getCode());
        perimeterEntity.getPrivileges().stream().map(p -> p.getCode()).forEach(p -> perimeter.getPrivileges().add(p));
        perimeterEntity.getPerimeters().stream().map(p -> convert(p)).forEach(p -> perimeter.getPerimeters().add(p));
        return perimeter;
    }

}
