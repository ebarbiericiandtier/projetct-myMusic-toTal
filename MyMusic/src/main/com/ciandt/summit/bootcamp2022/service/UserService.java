package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findByUsername(String username);

    User findById(UUID id);
    User save(User user);
}
