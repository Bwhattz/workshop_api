package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {


    @Query(
            value = "SELECT role.name FROM roles AS role" +
                    ""
    )
}
