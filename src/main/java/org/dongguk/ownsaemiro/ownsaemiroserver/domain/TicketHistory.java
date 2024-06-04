package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "ticket_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  티켓 기록 기본 속성  */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ETicketStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    /*  연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

}
