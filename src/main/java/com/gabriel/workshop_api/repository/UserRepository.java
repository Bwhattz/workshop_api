package com.gabriel.workshop_api.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query(
            value = "SELECT user.* FROM users AS user" +
                    "WHERE user.name = :name AND user.email = :email",
            nativeQuery = true
    )
    Optional<User> findByUser(@Param("name_user") String name, @Param("email_user") String email);
}
