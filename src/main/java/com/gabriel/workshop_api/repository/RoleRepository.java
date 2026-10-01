package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    @Query(
            value = "SELECT role.name FROM roles AS role" +
                    "WHERE role.name = :name",
            nativeQuery = true
    )
    Optional<Role> findByRole(@Param("nameRole") String role);
}
