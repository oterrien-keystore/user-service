package com.ote.user.persistence;

import com.ote.common.persistence.model.UserEntity;
import com.ote.common.persistence.repository.IUserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistenceService {

    @Autowired
    private IUserJpaRepository userJpaRepository;

    public UserEntity find(long id) throws UserNotFoundException {
        return Optional.ofNullable(userJpaRepository.findOne(id)).orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserEntity create(UserEntity entity){

    }

    public void delete(long id) {
        userJpaRepository.delete(id);
    }

    public class UserNotFoundException extends Exception {

        private static final String ErrorMessageTemplate = "User (%s=%s) is not found";

        public UserNotFoundException(long id) {
            super(String.format(ErrorMessageTemplate, "Id", id));
        }

        public UserNotFoundException(String login) {
            super(String.format(ErrorMessageTemplate, "Login", login));
        }
    }
}
