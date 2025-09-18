package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    // Spring Data JPA will generate the implementation based on the method name
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);
}
