package com.dexterv.eventticket.services.impl;

import com.dexterv.eventticket.domain.entities.Ticket;
import com.dexterv.eventticket.domain.entities.TicketStatusEnum;
import com.dexterv.eventticket.domain.entities.TicketType;
import com.dexterv.eventticket.domain.entities.User;
import com.dexterv.eventticket.exceptions.TicketTypeNotFoundException;
import com.dexterv.eventticket.exceptions.TicketsSoldOutException;
import com.dexterv.eventticket.exceptions.UserNotFoundException;
import com.dexterv.eventticket.repositories.TicketRepository;
import com.dexterv.eventticket.repositories.TicketTypeRepository;
import com.dexterv.eventticket.repositories.UserRepository;
import com.dexterv.eventticket.services.QrCodeService;
import com.dexterv.eventticket.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {
    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        // Look up the user
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(
                        String.format("User with id %s not found", userId)
                ));

        // Get ticket with pessimistic lock
        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException(
                        String.format("Ticket type with ID %s was not found", ticketTypeId)
                ));

        // Check ticket availability
        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchasedTickets + 1 > totalAvailable) {
            throw new TicketsSoldOutException();
        }

        // Crate new Ticket
        Ticket ticket = new Ticket();
        ticket.setTicketType(ticketType);
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setPurchaser(user);

        // Save and generate QR code
        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
