package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Ticket;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findFirstByEventAndStatus(Event event, ETicketStatus status);
}
