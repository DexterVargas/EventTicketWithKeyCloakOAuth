package com.dexterv.eventticket.services.impl;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.UpdateEventRequest;
import com.dexterv.eventticket.domain.UpdateTicketTypeRequest;
import com.dexterv.eventticket.domain.entities.Event;
import com.dexterv.eventticket.domain.entities.TicketType;
import com.dexterv.eventticket.domain.entities.User;
import com.dexterv.eventticket.exceptions.EventNotFoundException;
import com.dexterv.eventticket.exceptions.EventUpdateException;
import com.dexterv.eventticket.exceptions.TicketTypeNotFoundException;
import com.dexterv.eventticket.exceptions.UserNotFoundException;
import com.dexterv.eventticket.repositories.EventRepository;
import com.dexterv.eventticket.repositories.UserRepository;
import com.dexterv.eventticket.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        // Pass teh parameters to the repository method
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {

        if(null == event.getId()) {
            throw new EventUpdateException(String.format("Event with id %s not found", id));
        }

        if(!id.equals(event.getId())) {
            throw new EventUpdateException(String.format("Cannot update event with id %s not found", id));
        }

        Event existingEvent = eventRepository
                .findByIdAndOrganizerId(id, organizerId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id %s not found", id)));

        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        Set<UUID> requestTicketTypeIds = event.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(existingTicketType -> !requestTicketTypeIds.contains(existingTicketType.getId()));

        Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes()
                .stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest ticketType : event.getTicketTypes()) {
            if (null == ticketType.getId()) {
                // Create
                TicketType newTicketType = new TicketType();
                newTicketType.setName(ticketType.getName());
                newTicketType.setPrice(ticketType.getPrice());
                newTicketType.setDescription(ticketType.getDescription());
                newTicketType.setTotalAvailable(ticketType.getTotalAvailable());
                newTicketType.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(newTicketType);
            } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {
                TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setDescription(ticketType.getDescription());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());

            } else {
                throw new TicketTypeNotFoundException(String.format("Ticket type with id %s does not exist", ticketType.getId()));
            }
        }

        return eventRepository.save(existingEvent);
    }
}
