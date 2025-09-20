package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.QrCode;
import com.dexterv.eventticket.domain.entities.QrCodeStatusEnum;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository {
    QrCode saveAndFlush(QrCode qrCode);

    Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID ticketId, UUID ticketPurchaserId);

    Optional<QrCode> findByIdAndStatus(UUID qrCodeId, QrCodeStatusEnum qrCodeStatusEnum);
}
