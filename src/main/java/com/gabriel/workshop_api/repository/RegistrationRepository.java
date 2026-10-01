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
            value = "SELECT registration.* FROM registrations AS registration" +
                    "WHERE registration.registration_date > CURRENT_DATE AND registration.status = 'CONFIRM'",
            nativeQuery = true
    )
    List<Registration> findByFutureRegistration();

    @Query(
            value = "SELECT registration.* FROM registrations AS registration" +
                    "INNER JOIN events as event ON event.event_id = registration.event_id" +
                    "WHERE event.id = :eventId",
            nativeQuery = true
    )
    List<Registration> findByEvent(@Param("eventId") Integer eventId);
}
