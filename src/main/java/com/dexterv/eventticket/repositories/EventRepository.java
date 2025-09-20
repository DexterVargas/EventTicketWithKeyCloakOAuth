package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.Event;
import com.dexterv.eventticket.domain.entities.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    // Spring Data JPA will generate the implementation based on the method name
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

    Page<Event> findByStatus(EventStatusEnum status, Pageable pageable);

    Page<Event> searchPublishedEvents(String searchTerm, Pageable pageable);

    Optional<Event> findByIdAndStatus(UUID id, EventStatusEnum status);
}
