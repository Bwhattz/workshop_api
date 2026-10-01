package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    @Query(
            value = "SELECT event* FROM events AS event" +
                    "WHERE event.category_id = :categoryId",
            nativeQuery = true
    )
    Optional<Event> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query(
            value = "SELECT event.* FROM events AS event" +
                    "WHERE event.organize_id = event.organize_id AND event.event_date BETWEEN event.start_time AND event.end_time",
            nativeQuery = true
    )
    Optional<Event> findByConflictEvent(@Param("organizeId") Long organizaId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    @Query(
            value = "SELECT COUNT(*) FROM events AS event" +
                    "INNER JOIN registrations AS registration ON registration.event_id" +
                    "INNER JOIN ",
            nativeQuery = true
    )
    long countConfirmedRegistration(@Param("eventId") Long id);
}
