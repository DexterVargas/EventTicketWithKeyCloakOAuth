package com.dexterv.eventticket.services;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.UpdateEventRequest;
import com.dexterv.eventticket.domain.entities.Event;
import org.springframework.data.domain.Page;

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

//    Page<Event> deleteEventForOrganizer(UUID organizerId);
}
