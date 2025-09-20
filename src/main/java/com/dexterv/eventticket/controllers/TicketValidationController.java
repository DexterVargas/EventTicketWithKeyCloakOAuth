package com.dexterv.eventticket.controllers;


import com.dexterv.eventticket.domain.dtos.TicketValidationRequestDto;
import com.dexterv.eventticket.domain.dtos.TicketValidationResponseDto;
import com.dexterv.eventticket.domain.entities.TicketValidation;
import com.dexterv.eventticket.domain.entities.TicketValidationMethod;
import com.dexterv.eventticket.mappers.TicketValidationMapper;
import com.dexterv.eventticket.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1/ticket-validations")
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto) {

        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;

        if(TicketValidationMethod.MANUAL.equals(method)) {
            ticketValidation = ticketValidationService.validateTicketManually(
                    ticketValidationRequestDto.getId()
            );
        } else {
            ticketValidation = ticketValidationService.validateTicketByQrCode(
                    ticketValidationRequestDto.getId()
            );
        }

        return ResponseEntity.ok(
                (TicketValidationResponseDto) ticketValidationMapper.toTicketValidationResponseDto(ticketValidation)
        );
    }
}
