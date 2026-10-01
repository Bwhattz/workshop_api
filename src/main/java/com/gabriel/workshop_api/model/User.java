package com.gabriel.workshop_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idUser", nullable = false, unique = true)
    private String idUser;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @PrePersist
    public void generatedIdUser() {

        if(this.idUser == null || this.idUser.isBlank()) {
            this.idUser = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();
        }
    }

    public void update(User updateUser) {

        if(updateUser.getName() != null) {
            this.name = updateUser.getName();
        }

        if(updateUser.getEmail() != null) {
            this.email = updateUser.getEmail();
        }

        if(updateUser.getPassword() != null) {
            this.password = updateUser.getPassword();
        }
        if(updateUser.getPassword() != null) {
            this.password = updateUser.getPassword();
        }
    }
}
