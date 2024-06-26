package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "tickets")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    /*  티켓 기본 속성  */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "hash", nullable = false, length = 512)
//    private String hash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ETicketStatus status;

    @Column(name = "activated_at")
    private LocalDate activatedAt;

    @Column(name = "ticket_number")
    private Integer ticketNumber;

    /*  티켓 연관관계 속성  */
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /**
     * 테스트용 빌더
     */
    @Builder
    public Ticket(Integer ticketNumber, ETicketStatus status, Event event) {
        this.status = status;
        this.event = event;
        this.ticketNumber = ticketNumber;
    }

    public void chooseActivateDate(LocalDate activatedAt){
        this.activatedAt = activatedAt;
    }
    public void changeStatus(ETicketStatus status){
        this.status = status;
    }

}
