package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.QrCode;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository {
    QrCode saveAndFlush(QrCode qrCode);

    Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID ticketId, UUID ticketPurchaserId);
}
