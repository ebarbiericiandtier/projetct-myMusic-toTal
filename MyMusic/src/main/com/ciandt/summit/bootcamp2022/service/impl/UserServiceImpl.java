package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.exception.InvalidUserException;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.repository.UserRepository;
import com.ciandt.summit.bootcamp2022.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User findById(UUID id){

        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid user id");
                    return new InvalidUserException();
                });
    }

    @Override
    public User save(User user) {
        user.setPlaylist(playlistRepository.save(new Playlist()));
        return userRepository.save(user);
    }
}
