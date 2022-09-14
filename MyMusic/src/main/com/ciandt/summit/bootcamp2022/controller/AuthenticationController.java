package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.AuthRequest;
import com.ciandt.summit.bootcamp2022.service.TokenProviderService;
import com.ciandt.summit.bootcamp2022.util.EncodeUtil;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    final TokenProviderService tokenProviderService;

    @Operation(summary = "Generates an authentication token for the username provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Succesfully generated an access token"),
            @ApiResponse(responseCode = "404", description = "User for given username was not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Application error", content = @Content)
    })
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody  AuthRequest authRequest){
        String username = authRequest.getUsername();
        String token = tokenProviderService.getToken(username);
        String base64 = EncodeUtil.encodeAuth(username, token);
        logger.info("Successfully generated token");
        return ResponseEntity.status(201).body("Basic " + base64);
    }
}