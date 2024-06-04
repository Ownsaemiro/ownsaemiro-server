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

    @Column(name = "activated_at")
    private LocalDate activatedAt;

    /*  사용자 티켓 연관관계 속성  */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Builder
    public UserTicket(LocalDate activatedAt, User user, Ticket ticket) {
        this.activatedAt = activatedAt;
        this.user = user;
        this.ticket = ticket;
    }

}
