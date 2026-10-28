package com.gabriel.workshop_api.model;

import com.gabriel.workshop_api.emus.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_user_id", nullable = false)
    private User user;

    @PrePersist
    protected void prePersist() {
        if(registrationDate == null) {
            this.registrationDate = LocalDateTime.now();
        }

        if(status == null) {
            this.status = RegistrationStatus.CONFIRMED;
        }
    }
}
