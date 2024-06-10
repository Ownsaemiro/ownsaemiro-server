package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EUserTicketStatus;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@Table(name = "user_tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserTicket {

    /*  사용자 티켓 기본 속성  */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bought_at", nullable = false)
    private LocalDate boughtAt;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EUserTicketStatus status;

    /*  사용자 티켓 연관관계 속성  */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Builder
    public UserTicket(LocalDate boughtAt, String orderId, User user, Ticket ticket) {
        this.boughtAt = boughtAt;
        this.orderId = orderId;
        this.status = EUserTicketStatus.BEFORE_USE;
        this.user = user;
        this.ticket = ticket;
    }

    public void changeStatus(EUserTicketStatus status){
        this.status = status;
    }

}
