package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EAssignStatus;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_assign_tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserAssignTicket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EAssignStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Builder
    public UserAssignTicket(User user, Ticket ticket, LocalDate createdAt) {
        this.user = user;
        this.ticket = ticket;
        this.createdAt = createdAt;
        this.status = EAssignStatus.WAITING;
    }
}
