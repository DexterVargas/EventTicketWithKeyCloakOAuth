package com.dexterv.eventticket.mappers;

import com.dexterv.eventticket.domain.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    @Mapping(target = "ticketId", source = "ticket_id")
    TicketValidationMapper toTicketValidationResponseDto(TicketValidation ticketValidation);
}
