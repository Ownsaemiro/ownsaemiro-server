package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "event_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventRequest {
    @Id
    @Column(name = "id")
    private Long id;

    /*  행사 요청 기본 속성  */
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EEventRequestStatus state;

    /* 연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Builder
    public EventRequest(Long id, LocalDate createdAt, Event event) {
        this.id = id;
        this.createdAt = createdAt;
        this.event = event;
        this.state = EEventRequestStatus.WAITING;
    }
}
