package com.dexterv.eventticket.repositories;

import com.dexterv.eventticket.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Integer countByTicketTypeId(UUID id);

    Page<Ticket> findByPurchaserId(UUID purchaserId, Pageable pageable);
}
