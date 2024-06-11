package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EAssignStatus;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@Table(name = "user_assign_tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAssignTicket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

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
    public UserAssignTicket(User user, Ticket ticket) {
        this.user = user;
        this.ticket = ticket;
        this.createdAt = LocalDateTime.now();
        this.status = EAssignStatus.WAITING;
    }

    public void updateStatus(EAssignStatus status){
        this.status = status;
    }
}
