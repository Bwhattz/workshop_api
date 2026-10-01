package com.gabriel.workshop_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<User> users = new ArrayList<>();

    public void update(Role updateRole) {

        if(updateRole.getName() != null) {
            this.name = updateRole.getName();
        }

        if(updateRole.getUsers() != null) {
            this.users = updateRole.getUsers();
        }
    }
}
