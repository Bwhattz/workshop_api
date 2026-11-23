package com.gabriel.workshop_api.model;

import com.gabriel.workshop_api.emus.StatusProfile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private StatusProfile status;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role", nullable = false)
    private User user;

    public void update(Role updateRole) {

        if(updateRole.getStatus() != null) {
            this.status = updateRole.getStatus();
        }
    }
}
