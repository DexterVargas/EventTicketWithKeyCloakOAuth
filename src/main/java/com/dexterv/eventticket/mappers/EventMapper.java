package com.dexterv.eventticket.mappers;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.CreateTicketTypeRequest;
import com.dexterv.eventticket.domain.dtos.*;
import com.dexterv.eventticket.domain.entities.Event;
import com.dexterv.eventticket.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);
    ListEventResponseDto toLstEventResponseDto (Event event);
}
