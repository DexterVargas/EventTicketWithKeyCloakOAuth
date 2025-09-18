package com.dexterv.eventticket.controllers;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.dtos.CreateEventRequestDto;
import com.dexterv.eventticket.domain.dtos.CreateEventResponseDto;
import com.dexterv.eventticket.domain.dtos.ListEventResponseDto;
import com.dexterv.eventticket.domain.entities.Event;
import com.dexterv.eventticket.mappers.EventMapper;
import com.dexterv.eventticket.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.UUID;

import static com.dexterv.eventticket.util.JwtUtil.parseUserId;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid
            @RequestBody CreateEventRequestDto createEventRequestDto
    ) {
        // Convert DTO to doamin object
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);

        // Extract user ID from JWT
        UUID userId = UUID.fromString(jwt.getSubject());

        // Create the event
        Event createdEvent = eventService.createEvent(userId, createEventRequest);

        // Convert response to DTO
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);

        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        UUID userId = parseUserId(jwt);

        Page<Event> events = eventService.listEventsForOrganizer(userId, pageable);

        return ResponseEntity.ok(events.map(eventMapper::toLstEventResponseDto));
    }
}
