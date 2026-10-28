package com.gabriel.workshop_api.model;

import com.gabriel.workshop_api.emus.RoleType;
import com.gabriel.workshop_api.emus.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "user_id", nullable = false, unique = true)
    private String idUser;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private UserStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_user")
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {

        if (idUser == null || idUser.isBlank()) {
            this.idUser = "USER" + UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toLowerCase();
        }

        if(this.status == null) {
            this.status = UserStatus.ACTIVED;
        }
    }

    public void update(User updateUser) {

        if (updateUser.getName() != null) {
            this.name = updateUser.getName();
        }

        if (updateUser.getEmail() != null) {
            this.email = updateUser.getEmail();
        }

        if (updateUser.getPassword() != null) {
            this.password = updateUser.getPassword();
        }

        if (updateUser.getRoles() != null && !updateUser.getRoles().isEmpty()) {
            this.roles.clear();
            this.roles.addAll(updateUser.getRoles());
        }
    }
}
