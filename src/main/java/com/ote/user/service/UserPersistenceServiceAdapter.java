package com.ote.user.service;

import com.ote.user.request.Page;
import com.ote.user.crud.UserPayload;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class UserPersistenceServiceAdapter {

    public UserPayload findOne(long id){
        throw new NotImplementedException();
    }

    public Page<UserPayload> findAll(){
        throw new NotImplementedException();
    }

    public Page<UserPayload> findMany(UserPayload filter, String sortingBy, String sortingDirection, int pageSize, int pageIndex){
        throw new NotImplementedException();
    }

    public UserPayload create(UserPayload payload){
        throw new NotImplementedException();
    }

    public UserPayload merge(long id, UserPayload payload){
        throw new NotImplementedException();
    }

    public UserPayload patch(long id, UserPayload payload){
        throw new NotImplementedException();
    }

    public void deleteOne(long id) {
        throw new NotImplementedException();
    }

    public void deleteAll(){
        throw new NotImplementedException();
    }

    public void deleteMany(UserPayload filter){
        throw new NotImplementedException();
    }
}
