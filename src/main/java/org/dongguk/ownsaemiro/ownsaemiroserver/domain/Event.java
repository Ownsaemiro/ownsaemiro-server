package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Table(name = "events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  행사 정보 속성  */

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description", nullable = false, length = 3000)
    private String description;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "duration", nullable = false, length = 30)
    private String duration;

    @Column(name = "running_time", nullable = false)
    private Integer runningTime;

    @Column(name = "seat", nullable = false)
    private Integer seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ECategory category;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EEventStatus status;

    @Column(name = "rating")
    private String rating;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    /* 연관 관계 속성 */

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Event(String name, String description, String address, String duration, Integer runningTime, Integer seat, ECategory category, Integer price, String rating, EEventStatus status, Boolean isApproved, User user) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.duration = duration;
        this.runningTime = runningTime;
        this.seat = seat;
        this.category = category;
        this.price = price;
        this.status = status;
        this.rating = rating;
        this.isApproved = isApproved;
        this.user = user;
    }

    /**
     * 행사 상태 변경 함수
     */
    public EEventStatus changeStatus(EEventStatus status){
        this.status = status;
        return this.status;
    }

    /**
     * 행사 승인 상태 변경 함수
     */
    public void changeApproved(Boolean isApproved){
        this.isApproved = isApproved;
    }

}
