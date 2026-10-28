package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    @Query(
            value = "SELECT role.name FROM roles AS role " +
                    "WHERE role.name = :name",
            nativeQuery = true
    )
    Optional<Role> findByRole(@Param("name_role") String role);

    @Query(
            value = "SELECT role.* FROM roles AS role " +
                    "INNER JOIN user_roles AS user ON user.user_id " +
                    "WHERE user.role_user = :roleUser ",
            nativeQuery = true
    )
    List<User> findAllRolesByUser(@Param("roleId") Long roleId);
}
