package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.config.security.JwtService;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.request.UserRequest;
import com.gabriel.workshop_api.response.UserResponse;
import com.gabriel.workshop_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtToken;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) {

        User user = userRequest.toEntityUser();

        User service = userService.save(user);

        String token = jwtToken.generatedToken(service);
    }
}
