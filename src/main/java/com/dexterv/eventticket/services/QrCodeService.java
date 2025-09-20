package com.dexterv.eventticket.services;

import com.dexterv.eventticket.domain.entities.QrCode;
import com.dexterv.eventticket.domain.entities.Ticket;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
}
