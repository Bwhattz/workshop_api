package com.gabriel.workshop_api.repository;


import com.gabriel.workshop_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query(
            value = "SELECT user.* FROM users AS user " +
                    "WHERE user.name = :name AND user.email = :email",
            nativeQuery = true
    )
    Optional<User> findByUser(@Param("name_user") String name, @Param("email_user") String email);

    @Query(
            value = "SELECT COUNT(user.id) FROM users user " +
                    "WHERE user.user_email = :userEmail",
            nativeQuery = true
    )
    boolean existsByEmail(String email);

    @Query(
            value = "SELECT user.* FROM users user " +
                    "WHERE user.user_email = :userEmail",
            nativeQuery = true
    )
    Optional<User> findByUserEmail(String userEmail);

    @Query(
            value = "SELECT user.* FROM users AS user " +
                    "INNER JOIN user_roles AS user ON user_id = user.user_id " +
                    "INNER JOIN roles AS role ON role.id = role.role_id " +
                    "WHERE role.name = :name",
            nativeQuery = true
    )
    List<User> findByRolesNative(@Param("role_name") String roleName);
}
