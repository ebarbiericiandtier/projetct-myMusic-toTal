package java.com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.enums.Role;
import com.ciandt.summit.bootcamp2022.repository.UserRepository;
import com.ciandt.summit.bootcamp2022.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void when_insertNewUser_thenCreateEmptyPlaylist(){
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(answer -> answer.getArguments()[0]);

        User user = User.builder().id(new UUID(3, 10))
                                    .role(Role.PREMIUM)
                                    .username("maria")
                                    .build();

        User returnedUser = userService.save(user);

        Assertions.assertNotNull(returnedUser.getPlaylist(), "An empty playlist must be created for User");
    }
}
