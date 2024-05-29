package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "event_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  행사 요청 기본 속성  */
    private LocalDateTime createdAt;

    /* 연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
