package com.dexterv.eventticket.mappers;

import com.dexterv.eventticket.domain.CreateEventRequest;
import com.dexterv.eventticket.domain.CreateTicketTypeRequest;
import com.dexterv.eventticket.domain.dtos.CreateEventRequestDto;
import com.dexterv.eventticket.domain.dtos.CreateEventResponseDto;
import com.dexterv.eventticket.domain.dtos.CreateTicketTypeRequestDto;
import com.dexterv.eventticket.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventResponseDto toDto(Event event);
}
