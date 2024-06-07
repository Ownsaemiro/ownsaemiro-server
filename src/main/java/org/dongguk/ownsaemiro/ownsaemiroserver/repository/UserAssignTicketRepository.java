package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserAssignTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAssignTicketRepository extends JpaRepository<UserAssignTicket, Long> {

    @Query(value = "SELECT ut.* FROM ( " +
            "    SELECT " +
            "        ut.*, " +
            "        ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY RAND()) AS rn " +
            "    FROM " +
            "        user_assign_tickets ut " +
            "    INNER JOIN " +
            "        tickets t ON ut.ticket_id = t.id " +
            ") AS ut " + // 테이블 별칭 변경
            "WHERE " +
            "    rn = 1", nativeQuery = true)
    List<UserAssignTicket> findRandomUserAssignTicket();



    @Query("select ut from UserAssignTicket ut where ut.status = org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EAssignStatus.WAITING")
    List<UserAssignTicket> findFailToAssignTicket();
    Page<UserAssignTicket> findAllByUser(User user, Pageable pageable);
}
