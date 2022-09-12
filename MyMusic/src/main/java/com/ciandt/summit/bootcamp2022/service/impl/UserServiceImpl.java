package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.exception.InvalidUserException;
import com.ciandt.summit.bootcamp2022.repository.UserRepository;
import com.ciandt.summit.bootcamp2022.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User findById(String id){

        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid user id");
                    return new InvalidUserException();
                });
    }
}
