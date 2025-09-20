package com.dexterv.eventticket.services;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.UpdateEventRequest;
import com.dexterv.eventticket.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);

    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);

    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event);

    void deleteEventForOrganizer(UUID organizerId, UUID id);

    Page<Event> listPublishedEvents(Pageable pageable);

    @Query(value = "SELECT * FROM events WHERE " +
            "status = 'PUBLISHED' AND " +
            "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
            "@@ plainto_tsquery('english', :searchTerm)",
            countQuery = "SELECT count(*) FROM events WHERE " +
                    "status = 'PUBLISHED' AND " +
                    "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
                    "@@ plainto_tsquery('english', :searchTerm)",
            nativeQuery = true)
    Page<Event> searchPublishedEvents(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<Event> getPublishedEvent(UUID id);
}
