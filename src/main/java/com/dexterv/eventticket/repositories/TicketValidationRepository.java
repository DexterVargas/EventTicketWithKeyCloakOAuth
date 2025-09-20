package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {
}
