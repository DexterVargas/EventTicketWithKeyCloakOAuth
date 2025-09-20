package com.dexterv.eventticket.mappers;

import com.dexterv.eventticket.domain.dtos.ListTicketResponseDto;
import com.dexterv.eventticket.domain.dtos.ListTicketTicketTypeResponseDto;
import com.dexterv.eventticket.domain.entities.Ticket;
import com.dexterv.eventticket.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);
    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
