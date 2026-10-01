package com.gabriel.workshop_api.repository;


import com.gabriel.workshop_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query(
            value = "SELECT user.* FROM users AS user" +
                    "WHERE user.name = :name AND user.email = :email",
            nativeQuery = true
    )
    Optional<User> findByUser(@Param("name_user") String name, @Param("email_user") String email);

    boolean existsByEmail(String email);

    @Query(
            value = "SELECT user.* FROM users AS user" +
                    "INNER JOIN user_roles AS user ON user_id = user.user_id" +
                    "INNER JOIN roles AS role ON role.id = role.role_id" +
                    "WHERE role.name = :name",
            nativeQuery = true
    )
    List<User> findByRolesNative(@Param("roleName") String roleName);
}
