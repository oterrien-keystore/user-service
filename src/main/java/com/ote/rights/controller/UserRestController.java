package com.ote.rights.controller;

import com.ote.common.persistence.model.IRightDetail;
import com.ote.common.persistence.model.UserRightEntity;
import com.ote.common.persistence.repository.IUserRightJpaRepository;
import com.ote.user.rights.api.IUserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private IUserRightService userRightService;

    @Autowired
    private IUserRightJpaRepository userRightJpaRepository;

    @GetMapping(value = "/{id}/rights", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserRightOrganizedPayload> getRights(@PathVariable("id") long id) {
        List<UserRightEntity> userRights = userRightJpaRepository.findByUserIdWithDetails(id);
        return userRights.stream().
                map(p -> {
                    UserRightOrganizedPayload userRightOrganizedPayload = getUserRights(p);
                    populatePerimeter(userRightOrganizedPayload.getPerimeters(), p.getDetails());
                    return userRightOrganizedPayload;
                }).collect(Collectors.toList());
    }

    private UserRightOrganizedPayload getUserRights(UserRightEntity userRightEntity) {
        UserRightOrganizedPayload userRightOrganizedPayload = new UserRightOrganizedPayload();
        userRightOrganizedPayload.setApplication(userRightEntity.getApplication().getCode());
        userRightOrganizedPayload.setPerimeters(new HashSet<>());
        return userRightOrganizedPayload;
    }

    private <T extends IRightDetail> void populatePerimeter(Collection<UserRightOrganizedPayload.Perimeter> perimeters, Collection<T> rightDetails) {
        rightDetails.forEach(d -> populatePerimeter(perimeters, d));
    }

    private void populatePerimeter(Collection<UserRightOrganizedPayload.Perimeter> perimeters, IRightDetail rightDetail) {
        UserRightOrganizedPayload.Perimeter perimeter =
                perimeters.stream().
                        filter(pe -> pe.getCode().equalsIgnoreCase(rightDetail.getPerimeter().getCode())).
                        findFirst().
                        orElseGet(() -> {
                            UserRightOrganizedPayload.Perimeter per = new UserRightOrganizedPayload.Perimeter();
                            per.setCode(rightDetail.getPerimeter().getCode());
                            per.setPrivileges(new HashSet<>());
                            perimeters.add(per);
                            return per;
                        });

        rightDetail.getPrivileges().forEach(pri -> {
            perimeter.getPrivileges().add(pri.getCode());
        });
    }

    @PutMapping(value = "/{id}/rights", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void addRights(@PathVariable("id") long id, @RequestBody UserRightPayload payload) {

    }

    @DeleteMapping(value = "/{id}/rights", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void removeRights(@PathVariable("id") long id, @RequestBody UserRightPayload payload) {

    }


}
