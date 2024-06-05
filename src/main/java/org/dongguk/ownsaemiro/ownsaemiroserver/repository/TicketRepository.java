package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Ticket;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * 양도 가능한 티켓 조회 -> 전체 조회
     */
    @Query("select t from Ticket t " +
            "where t.status = org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus.TRANSFER ")
    Page<Ticket> findAssignTickets(Pageable pageable);

    /**
     * 양도 가능한 티켓 조회 -> 카테고리 조회
     */
    @Query("select t from Ticket t " +
            "where t.status = org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus.TRANSFER " +
                    "and t.event.category = :category")
    Page<Ticket> findAssignTicketsByCategory(ECategory category, Pageable pageable);

    /**
     * 특정 행사에 대해 양도가 가능한 티켓 조회
     */
    Optional<Ticket> findFirstByEventAndStatus(Event event, ETicketStatus status);
}
