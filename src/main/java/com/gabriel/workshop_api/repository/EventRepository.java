package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    @Query(
            value = "SELECT * FROM event AS event" +
                    "WHERE event.category_id = :categoryId",
            nativeQuery = true
    )
    Optional<Event> findByCategoryId(@Param("category_id") Long categoryId);


    @Query(
            value = "SELECT COUNT(*) FROM events AS events" +
                    "INNER JOIN registrations AS registration ON registration.event_id ",
            nativeQuery = true
    )
    long countConfirmedRegistration(@Param("event_id") Long id);
}
