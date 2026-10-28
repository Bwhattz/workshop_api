package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.request.RoleRequest;
import com.gabriel.workshop_api.response.RoleResponse;
import com.gabriel.workshop_api.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid RoleRequest roleRequest) {

        Role role = roleRequest.toEntity();

        Role serviceRole = roleService.save(role);

        RoleResponse roleResponse = RoleResponse.fromRole(serviceRole);

        return ResponseEntity.status(HttpStatus.CREATED).body(roleResponse);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getRoles() {

        List<RoleResponse> listRole = roleService.findAll().stream()
                .map(RoleResponse::fromRole)
                .toList();

        return ResponseEntity.ok().body(listRole);

    }

    @GetMapping(value = "/{nameRole}")
    public ResponseEntity<RoleResponse> findByRole(@PathVariable String nameRole) {

        return roleService.findByRole(nameRole)
                .map(RoleResponse::fromRole)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping
    public ResponseEntity<RoleResponse> updateRole(@RequestBody @Valid RoleRequest roleRequest) {

        Role roleEntity = roleRequest.toEntity();

        Role serviceRole = roleService.update(roleEntity);

        RoleResponse roleResponse = RoleResponse.fromRole(serviceRole);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleResponse);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {

        roleService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
