package com.dexterv.eventticket.services.impl;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.entities.Event;
import com.dexterv.eventticket.domain.entities.TicketType;
import com.dexterv.eventticket.domain.entities.User;
import com.dexterv.eventticket.exceptions.UserNotFoundException;
import com.dexterv.eventticket.repositories.EventRepository;
import com.dexterv.eventticket.repositories.UserRepository;
import com.dexterv.eventticket.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        // Find the organizer or throw an exception if not found
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %s not found", organizerId)
                ));

        // Create and populate the event
        Event eventToCreate = new Event();

        // Create ticket types
        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType newTicketType = new TicketType();
                    newTicketType.setName(ticketType.getName());
                    newTicketType.setPrice(ticketType.getPrice());
                    newTicketType.setDescription(ticketType.getDescription());
                    newTicketType.setTotalAvailable(ticketType.getTotalAvailable());
                    newTicketType.setEvent(eventToCreate);
                    return newTicketType;
                }).toList();

        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setTicketTypes(ticketTypesToCreate);
        eventToCreate.setOrganizer(organizer);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        // use the repository to find events by organizer ID with pagination
        return  eventRepository.findByOrganizerId(organizerId, pageable);
    }
}
