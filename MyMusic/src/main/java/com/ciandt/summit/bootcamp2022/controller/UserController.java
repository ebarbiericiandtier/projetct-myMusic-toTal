package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.service.UserService;
import com.ciandt.summit.bootcamp2022.service.mapper.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserDTOMapper userDTOMapper;

    @GetMapping("/{userId}")
    ResponseEntity<UserDTO> findById(@PathVariable("userId") String id){
        UserDTO userDTO =
                userDTOMapper.toDto(userService.findById(id));
        return ResponseEntity.ok(
                userDTO
        );
    }
}
