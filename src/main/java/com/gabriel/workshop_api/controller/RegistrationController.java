package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.model.Registration;
import com.gabriel.workshop_api.request.RegistrationRequest;
import com.gabriel.workshop_api.request.RoleRequest;
import com.gabriel.workshop_api.response.RegistrationResponse;
import com.gabriel.workshop_api.response.RoleResponse;
import com.gabriel.workshop_api.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> createRegistration(@RequestBody @Valid RegistrationRequest registrationRequest) {

        Registration registration = registrationRequest.toEntity();

        Registration serviceRegistration = registrationService.save(registration);

        RegistrationResponse registrationResponse = RegistrationResponse.fromRegistration(serviceRegistration);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResponse>> getRegistration() {

        List<RegistrationResponse> responseList = registrationService.findAll().stream()
                .map(RegistrationResponse::fromRegistration)
                .toList();

        return ResponseEntity.ok().body(responseList);
    }

    @PutMapping
    public ResponseEntity<RegistrationResponse> updateRegistration(@RequestBody @Valid RegistrationRequest registrationRequest) {

        Registration registration = registrationRequest.toEntity();

        Registration serviceRegistration = registrationService.update(registration);

        RegistrationResponse registrationResponse = RegistrationResponse.fromRegistration(serviceRegistration);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(registrationResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {

        registrationService.deleteId(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/future")
    public ResponseEntity<List<RegistrationResponse>> getFutureRegistrations() {
        List<RegistrationResponse> listRegistrations = registrationService.findByFutureRegistration().stream()
                .map(RegistrationResponse::fromRegistration)
                .toList();
        return ResponseEntity.ok(listRegistrations);
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity<RegistrationResponse> getRegistrationByEvent(@PathVariable Long eventId) {
        return registrationService.findByEvent(eventId)
                .map(RegistrationResponse::fromRegistration)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
