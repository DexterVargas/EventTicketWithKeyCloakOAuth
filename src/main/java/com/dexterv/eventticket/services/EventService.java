package com.dexterv.eventticket.services;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.entities.Event;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
}
