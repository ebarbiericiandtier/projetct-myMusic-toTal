package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.UserDto;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.service.UserService;
import com.ciandt.summit.bootcamp2022.service.mapper.UserDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Operation(summary = "Saves a new user in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a link for created user",
                    content = @Content),
    })
    @PostMapping
    ResponseEntity<?> saveUser(@Valid @RequestBody UserDto userDto){
        logger.info("Receive request for creation of user {}", userDto);
        final User user = userDTOMapper.toEntity(userDto);
        final String id = String.valueOf(userService.save(user).getId());
        return ResponseEntity.created(ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(id)
                                .toUri())
                                .build();
    }
}
