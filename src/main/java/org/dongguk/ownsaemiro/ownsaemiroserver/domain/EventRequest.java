package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Table(name = "event_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventRequest {
    @Id
    @Column(name = "id")
    private Long id;

    /*  행사 요청 기본 속성  */
    @Column(name = "seat", nullable = false)
    private Integer seat;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EEventRequestStatus state;

    /* 연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Builder
    public EventRequest(Long id, Integer seat, LocalDate createdAt, Event event, User user) {
        this.id = id;
        this.seat = seat;
        this.createdAt = createdAt;
        this.event = event;
        this.user = user;
        this.state = EEventRequestStatus.WAITING;
    }

    public EEventRequestStatus updateStatus(EEventRequestStatus eEventRequestStatus){
        this.state = eEventRequestStatus;
        return this.state;
    }
}
