package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.config.security.JwtService;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.request.UserRequest;
import com.gabriel.workshop_api.response.UserResponse;
import com.gabriel.workshop_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        String tokenUser = jwtToken.generateToken(service);

        UserResponse userResponse = UserResponse.fromUser(service);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + tokenUser)
                .body(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {

        List<UserResponse> findAllUsers = userService.findAll().stream()
                .map(UserResponse::fromUser)
                .toList();

        return ResponseEntity.ok().body(findAllUsers);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUsers(@RequestBody @Valid UserRequest userRequest) {

        User user = userRequest.toEntityUser();

        User serviceUser = userService.update(user);

        UserResponse userResponse = UserResponse.fromUser(serviceUser);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{name}/{email}")
    public ResponseEntity<UserResponse> findByUsers(@PathVariable String name, @PathVariable String email) {

        return userService.findByUser(name, email)
                .map(UserResponse::fromUser)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(value = "role/{role}")
    public ResponseEntity<List<UserResponse>> findByRoles(@PathVariable String name) {

        List<UserResponse> listRoles = userService.findByRoles(name).stream()
                .map(UserResponse::fromUser)
                .toList();

        return ResponseEntity.ok().body(listRoles);
    }
}
