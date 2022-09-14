package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.enums.Role;
import com.ciandt.summit.bootcamp2022.exception.InvalidUserException;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.repository.UserRepository;
import com.ciandt.summit.bootcamp2022.service.mapper.UserDTOMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private UserDTOMapper INSTANCE = UserDTOMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, playlistRepository);
    }

    @Test
    void when_findById_thenReturnUser() {
        User user = new User();

        user.setId(new UUID(3, 10));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));

        User actual = userService.findById(user.getId());

        UserDTO expected = INSTANCE.toDto(user);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void when_notFindById_thenReturnUserNotFound() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        InvalidUserException thrown = assertThrows(
                InvalidUserException.class,
                () -> userService.findById(any()),
                "Exception not found"
        );

        assertEquals(thrown.getClass(), InvalidUserException.class);
    }

    @Test
    void when_findByUsername_thenReturnUsername() {
        User user = new User();

        user.setUsername("jose");

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByUsername(user.getUsername());

        UserDTO expected = INSTANCE.toDto(user);

        assertEquals(expected.getUsername(), actual.get().getUsername());
    }

    @Test
    void when_insertNewUser_thenCreateEmptyPlaylist(){
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(answer -> answer.getArguments()[0]);
        Mockito.when(playlistRepository.save(Mockito.any(Playlist.class))).thenAnswer(answer -> answer.getArguments()[0]);

        User user = User.builder().id(new UUID(3, 10))
                                    .role(Role.PREMIUM)
                                    .username("maria")
                                    .build();

        User returnedUser = userService.save(user);

        Assertions.assertNotNull(returnedUser.getPlaylist(), "An empty playlist must be created for User");
    }
}
