package com.dexterv.eventticket.services;

import com.dexterv.eventticket.domain.dtos.CreateEventRequest;
import com.dexterv.eventticket.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
