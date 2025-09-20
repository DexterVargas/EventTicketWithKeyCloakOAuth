package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.QrCode;
import org.springframework.stereotype.Repository;

@Repository
public interface QrCodeRepository {
    QrCode saveAndFlush(QrCode qrCode);
}
