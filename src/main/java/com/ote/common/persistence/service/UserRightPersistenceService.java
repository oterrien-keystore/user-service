package com.ote.common.persistence.service;

import com.ote.common.Scope;
import com.ote.common.controller.UserRightDetailPayload;
import com.ote.common.persistence.model.IRightDetail;
import com.ote.common.persistence.model.PrivilegeEntity;
import com.ote.common.persistence.model.SecurityGroupRightEntity;
import com.ote.common.persistence.model.UserRightDetailEntity;
import com.ote.common.persistence.repository.ISecurityGroupRightJpaRepository;
import com.ote.common.persistence.repository.IUserRightJpaRepository;
import com.ote.crud.model.Filters;
import com.ote.crud.model.SortingParameters;
import com.ote.crud.model.SplitListParameter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class UserRightPersistenceService extends AdtPersistenceService<UserRightDetailPayload, UserRightDetailEntity> {

    @Autowired
    private IUserRightJpaRepository userRightJpaRepository;

    @Autowired
    private ISecurityGroupRightJpaRepository securityGroupRightRepository;

    @Getter
    private String scope = Scope.UserRight.name();

    public List<UserRightDetailPayload> findMany(String user, String application,
                                                 Filters filters,
                                                 SortingParameters sortingParameters,
                                                 SplitListParameter splitListParam) {


        if (!isRoleDefined(user, application)) {
            return Collections.emptyList();
        }

        // TODO: Duplication with UserRightPeristenceServiceAdapter
        List<IRightDetail> rightDetails = new ArrayList<>();

        // Retrieve user's rights for the given application
        Optional.ofNullable(userRightJpaRepository.findByUserLoginAndApplicationCodeWithDetails(user, application)).
                ifPresent(p -> rightDetails.addAll(p.getDetails()));

        // Retrieve user's rights inherited from its securityGroups for the given application
        List<SecurityGroupRightEntity> securityGroupRights =
                securityGroupRightRepository.findByUserLoginAndApplicationCodeWithDetails(user, application);
        securityGroupRights.stream().
                map(SecurityGroupRightEntity::getDetails).
                forEach(p -> rightDetails.addAll(p));
        return new PerimeterAggregator(rightDetails).get();
    }

    private boolean isRoleDefined(String user, String application) {
        return userRightJpaRepository.existsByUserLoginAndApplicationCode(user, application) ||
                securityGroupRightRepository.existsByUserLoginAndApplicationCode(user, application);
    }

    private class PerimeterAggregator implements Supplier<List<UserRightDetailPayload>> {

        private Map<String, UserRightDetailPayload> userRightDetailPayloadsMap = new HashMap<>();

        PerimeterAggregator(List<IRightDetail> details) {

            details.forEach(p -> {
                String code = p.getPerimeter().getCode();
                UserRightDetailPayload userRightDetailPayload =
                        Optional.ofNullable(userRightDetailPayloadsMap.get(code)).
                                orElseGet(() -> {
                                    UserRightDetailPayload p1 = new UserRightDetailPayload();
                                    p1.setPerimeter(code);
                                    p1.setPrivileges(new ArrayList<>());
                                    userRightDetailPayloadsMap.put(code, p1);
                                    return p1;
                                });
                p.getPrivileges().
                        stream().
                        map(PrivilegeEntity::getCode).
                        forEach(pr -> userRightDetailPayload.getPrivileges().add(pr));
            });
        }

        public List<UserRightDetailPayload> get() {
            return new ArrayList<>(userRightDetailPayloadsMap.values());
        }
    }
}
