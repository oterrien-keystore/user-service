package com.ote.user.service;

import com.ote.common.persistence.model.UserEntity;
import com.ote.user.crud.UserPayload;
import org.springframework.stereotype.Service;

@Service
public class UserMapperService {

    /*@Autowired
    private UserRightMapperService userRightMapperService;*/

    public UserPayload convert(UserEntity entity) {

        UserPayload payload = new UserPayload();
        payload.setId(entity.getId());
        payload.setLogin(entity.getLogin());
        payload.setPassword(entity.getPassword());
        //payload.setUserRights(userRightMapperService.conver(entity.get);
        return payload;
    }

    public UserEntity convert(UserPayload payload) {

        UserEntity entity = new UserEntity();
        entity.setId(payload.getId());
        entity.setLogin(payload.getLogin());
        entity.setPassword(payload.getPassword());
        //entity.setUserRights();
        return entity;
    }
}
