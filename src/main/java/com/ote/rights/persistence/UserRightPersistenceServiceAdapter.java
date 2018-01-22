package com.ote.rights.persistence;

import com.ote.common.persistence.model.IRightDetail;
import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.common.persistence.model.SecurityGroupRightEntity;
import com.ote.common.persistence.model.UserRightEntity;
import com.ote.common.persistence.repository.*;
import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.spi.IUserRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class UserRightPersistenceServiceAdapter implements IUserRightRepository {

    @Autowired
    private IUserJpaRepository userJpaRepository;

    @Autowired
    private IApplicationJpaRepository applicationJpaRepository;

    @Autowired
    private IPerimeterJpaRepository perimeterJpaRepository;

    @Autowired
    private IPrivilegeJpaRepository privilegeJpaRepository;

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
        return userRightJpaRepository.existsByUserLoginAndApplicationCode(user, application) ||
                securityGroupRightRepository.existsByUserLoginAndApplicationCode(user, application);
    }

    @Override
    public List<Perimeter> getPerimeters(String user, String application) {

        List<IRightDetail> rightDetails = new ArrayList<>();

        // Retrieve user's rights for the given application
        Optional.ofNullable(userRightJpaRepository.findByUserLoginAndApplicationCodeWithDetails(user, application)).
                map(UserRightEntity::getDetails).
                ifPresent(p -> rightDetails.addAll(p));

        // Retrieve user's rights inherited from its securityGroups for the given application
        securityGroupRightRepository.findByUserLoginAndApplicationCodeWithDetails(user, application).
                stream().
                map(SecurityGroupRightEntity::getDetails).
                forEach(p -> rightDetails.addAll(p));

        return new PerimeterAggregator(rightDetails).get();
    }

    private class PerimeterAggregator implements Supplier<List<Perimeter>> {

        private Map<String, Perimeter> perimetersMap = new HashMap<>();

        PerimeterAggregator(List<IRightDetail> details) {

            details.forEach(p -> {
                String code = p.getPerimeter().getCode();
                Perimeter perimeter =
                        Optional.ofNullable(perimetersMap.get(code)).
                                orElseGet(() -> {
                                    Perimeter p1 = new Perimeter(code);
                                    perimetersMap.put(code, p1);
                                    return p1;
                                });

                p.getPrivileges().
                        stream().
                        map(PrivilegeEntity::getCode).
                        forEach(pr -> perimeter.getPrivileges().add(pr));
            });
        }

        public List<Perimeter> get() {
            return new ArrayList<>(perimetersMap.values());
        }
    }

    @Override
    public boolean isPerimeterDefined(Path perimeterPath) {
        return perimeterJpaRepository.existsByPath(perimeterPath);
    }

    @Override
    public boolean isPrivilegeDefined(String privilege) {
        return privilegeJpaRepository.existsByCode(privilege);
    }

    @Override
    public Privilege getPrivilegeHierarchy(String privilege) {
        return Optional.ofNullable(privilegeJpaRepository.findByCode(privilege)).
                map(this::convert).
                orElse(null);
    }

    private Privilege convert(PrivilegeEntity entity) {
        if (entity.getParent() != null) {
            return new Privilege(entity.getCode(), convert(entity.getParent()));
        } else {
            return new Privilege(entity.getCode());
        }
    }

    @Override
    public void put(String user, String application, Perimeter perimeter) {

    }
}
