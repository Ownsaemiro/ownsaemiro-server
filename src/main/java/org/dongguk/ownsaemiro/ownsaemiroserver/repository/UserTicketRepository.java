package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Ticket;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {

    Optional<UserTicket> findByUserAndTicket(User user, Ticket ticket);
    @Query("select ut.orderId from UserTicket ut where ut.user = :user and ut.ticket = :ticket")
    Optional<String> findOrderIdByUserAndTicket(User user, Ticket ticket);
    Page<UserTicket> findAllByUser(User user, Pageable pageable);


}
