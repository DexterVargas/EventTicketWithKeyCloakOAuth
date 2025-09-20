package com.dexterv.eventticket.services;

import com.dexterv.eventticket.domain.entities.QrCode;
import com.dexterv.eventticket.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
