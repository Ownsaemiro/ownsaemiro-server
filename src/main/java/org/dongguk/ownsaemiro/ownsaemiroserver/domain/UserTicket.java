package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserTicket {

    /*  사용자 티켓 기본 속성  */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    /*  사용자 티켓 연관관계 속성  */
    @ManyToOne
    @Column(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @Column(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Builder
    public UserTicket(LocalDate createdAt, User user, Ticket ticket) {
        this.createdAt = createdAt;
        this.user = user;
        this.ticket = ticket;
    }

}
