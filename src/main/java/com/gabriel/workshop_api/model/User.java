package com.gabriel.workshop_api.model;

import com.gabriel.workshop_api.config.security.UserService;
import com.gabriel.workshop_api.emus.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_code", nullable = false, unique = true)
    private String codeUser;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @Column(name = "created_at", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    private UserStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "role_user")
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {

        if (codeUser == null || codeUser.isBlank()) {
            this.codeUser = "USER" + UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toLowerCase();
        }

        if(this.status == null) {
            this.status = UserStatus.ACTIVED;
        }

        if(this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getStatus().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
