package com.dexterv.eventticket.services.impl;

import com.dexterv.eventticket.domain.entities.*;
import com.dexterv.eventticket.exceptions.QrCodeNotFoundException;
import com.dexterv.eventticket.exceptions.TicketTypeNotFoundException;
import com.dexterv.eventticket.repositories.QrCodeRepository;
import com.dexterv.eventticket.repositories.TicketRepository;
import com.dexterv.eventticket.repositories.TicketValidationRepository;
import com.dexterv.eventticket.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(()-> new QrCodeNotFoundException(
                        String.format("QR Code with ID %s was not found", qrCodeId)
                ));
        
        Ticket ticket = qrCode.getTicket();
        
        return validateTicket(ticket);
    }

    private TicketValidation validateTicket(Ticket ticket) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationMethod.QR_SCAN);

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter(v->TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst()
                .map(v -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatus);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketTypeNotFoundException::new);

        return validateTicket(ticket);
    }
}
