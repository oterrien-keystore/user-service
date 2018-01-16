package com.ote.user.persistence;

import com.ote.common.persistence.model.*;
import com.ote.common.persistence.repository.*;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.spi.IUserRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserRightPersistenceServiceAdapter implements IUserRightRepository {


    @Autowired
    private IUserJpaRepository userJpaRepository;

    @Autowired
    private IApplicationJpaRepository applicationJpaRepository;

    @Autowired
    private IUserRightJpaRepository userRightJpaRepository;

    @Autowired
    private ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private ISecurityGroupRightJpaRepository securityGroupRightRepository;

    @Autowired
    private ISecurityGroupRightDetailJpaRepository securityGroupRightDetailRepository;

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

        // Retrieve user's rights for the given application
        UserRightEntity userRightEntity = userRightJpaRepository.findByUserLoginAndApplicationCodeWithDetails(user, application);
        List<IRightDetail> rightDetails = new ArrayList<>(userRightEntity.getDetails());

        // Retrieve user's rights inherited from its securityGroups for the given application
        UserEntity userEntity = userJpaRepository.findByLogin(user);
        List<SecurityGroupRightEntity> securityGroupRights = securityGroupRightRepository.findByUserAndApplicationCode(userEntity, application);
        rightDetails.addAll(securityGroupRights.stream().map(SecurityGroupRightEntity::getDetails).flatMap(Collection::stream).collect(Collectors.toSet()));

        return new PerimeterAggregator(rightDetails).getPerimeters();
    }

    private class PerimeterAggregator {

        private Map<String, Perimeter> perimeters = new HashMap<>();

        public PerimeterAggregator(List<IRightDetail> details) {
            // Split Perimeter by code
            details.stream().
                    map(IRightDetail::getPerimeter).
                    distinct().
                    map(PerimeterEntity::getCode).
                    forEach(code -> {
                        if (!perimeters.containsKey(code)) {
                            perimeters.put(code, new Perimeter(code));
                        }
                    });
            details.stream().
                    forEach(detailEntity -> {
                        String code = detailEntity.getPerimeter().getCode();
                        Perimeter perimeter = perimeters.get(code);
                        perimeter.getPrivileges().addAll(detailEntity.getPrivileges().stream().
                                map(PrivilegeEntity::getCode).
                                collect(Collectors.toList()));
                    });

            /*details.stream().
                    peek(detailEntity -> {
                        String code = detailEntity.getPerimeter().getCode();
                        Perimeter perimeter = perimeters.get(code);
                        perimeter.getPrivileges().addAll(detailEntity.getPrivileges().stream().
                                map(PrivilegeEntity::getCode).
                                collect(Collectors.toList()));
                    }).// Add privileges foreach
                    filter(detailEntity -> Optional.ofNullable(detailEntity.getPerimeter().getParent()).isPresent()).// Set parent if any (need map already built)
                    filter(detailEntity -> perimeters.containsKey(detailEntity.getPerimeter().getParent().getCode())).
                    forEach(detailEntity -> {
                        String code = detailEntity.getPerimeter().getCode();
                        String parent = detailEntity.getPerimeter().getParent().getCode();
                        Perimeter perimeter = perimeters.get(code);
                        Perimeter parentPerimeter = perimeters.get(parent);
                        parentPerimeter.getPerimeters().add(perimeter);
                    });*/
        }

        public List<Perimeter> getPerimeters() {
            return perimeters.values().stream().filter(distinctByKey(p -> p.getCode())).collect(Collectors.toList());
        }

        private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
            Map<Object, Boolean> map = new ConcurrentHashMap<>();
            return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
        }
    }

}
