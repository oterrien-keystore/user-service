package com.ote.rights.persistence;

import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.common.persistence.model.SecurityGroupRightDetailEntity;
import com.ote.common.persistence.model.SecurityGroupRightEntity;
import com.ote.common.persistence.repository.*;
import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.spi.IRightCheckerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class UserRightPersistenceServiceAdapter implements IRightCheckerRepository {

    @Autowired
    private IUserJpaRepository userJpaRepository;

    @Autowired
    private IApplicationJpaRepository applicationJpaRepository;

    @Autowired
    private IPerimeterJpaRepository perimeterJpaRepository;

    @Autowired
    private IPrivilegeJpaRepository privilegeJpaRepository;

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
        return securityGroupRightRepository.existsByUserLoginAndApplicationCode(user, application);
    }

    @Override
    public List<Perimeter> getPerimeters(String user, String application) {

        // Retrieve user's rights inherited from its securityGroups for the given application
        List<SecurityGroupRightDetailEntity> rightDetails =
                securityGroupRightRepository.findByUserLoginAndApplicationCodeWithDetails(user, application).
                        stream().
                        map(SecurityGroupRightEntity::getDetails).
                        flatMap(Collection::stream).
                        collect(Collectors.toList());

        return new PerimeterAggregator(rightDetails).get();
    }

    private static class PerimeterAggregator implements Supplier<List<Perimeter>> {

        private Map<String, Perimeter> perimetersMap = new HashMap<>();

        PerimeterAggregator(List<SecurityGroupRightDetailEntity> details) {

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
}
