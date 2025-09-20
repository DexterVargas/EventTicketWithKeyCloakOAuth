package com.dexterv.eventticket.services.impl;

import com.dexterv.eventticket.domain.entities.QrCode;
import com.dexterv.eventticket.domain.entities.QrCodeStatusEnum;
import com.dexterv.eventticket.domain.entities.Ticket;
import com.dexterv.eventticket.exceptions.QrCodeGenerationException;
import com.dexterv.eventticket.exceptions.QrCodeNotFoundException;
import com.dexterv.eventticket.repositories.QrCodeRepository;
import com.dexterv.eventticket.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public QrCode generateQrCode(Ticket ticket) {

        try {

            // Generate a unique ID for the QR code
            UUID uniqueId = UUID.randomUUID();
            String qrCodeImage = generateQrCodeImage(uniqueId);
            // Create and save the QR code entity
            QrCode qrCode = new QrCode();
            qrCode.setId(uniqueId);
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            qrCode.setTicket(ticket);
            qrCode.setValue(qrCodeImage);

            return qrCodeRepository.saveAndFlush(qrCode);
        } catch (IOException | WriterException e) {
            throw new QrCodeGenerationException("Failed to generate QR Code",e);
        }
    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        // Create a bit matrix for the QR Code
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );

        // Convert to BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Convert to base64 string
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode = qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)
                .orElseThrow(QrCodeNotFoundException::new);

        try {
            return Base64.getDecoder().decode(qrCode.getValue());
        } catch (IllegalArgumentException e) {
            log.error("Invalid base64 QR Code for ticket ID: {}", ticketId, e);
            throw new QrCodeGenerationException("Failed to generate QR Code",e);
        }
    }
}
