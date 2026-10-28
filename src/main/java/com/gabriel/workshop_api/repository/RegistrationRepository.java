package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {


    @Query(
            value = "SELECT registration.* FROM registrations AS registration " +
                    "WHERE registration.registration_date > CURRENT_DATE AND registration.status = 'CONFIRM'",
            nativeQuery = true
    )
    List<Registration> findByFutureRegistration();

    @Query(
            value = "SELECT registration.* FROM registrations AS registration " +
                    "WHERE registration.registration_user_id = :userId AND registration.registration_event_id = :eventId",
            nativeQuery = true
    )
    Optional<Registration> findByUserAndEvent(@Param("user_id") Long userId, @Param("event_id") Long eventId);

    @Query(
            value = "SELECT registration.* FROM registrations AS registration " +
                    "INNER JOIN events as event ON event.id = registration.event_id " +
                    "WHERE registration.event_id = :event",
            nativeQuery = true
    )
    Optional<Registration> findByEvent(@Param("event_id") Long eventId);
}
