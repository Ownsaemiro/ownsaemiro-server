package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;

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

    @Column(name = "brief", length = 100)
    private String brief;

    @Column(name = "description", nullable = false, length = 3000)
    private String description;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "duration", nullable = false, length = 30)
    private String duration;

    @Column(name = "running_time", nullable = false)
    private Integer runningTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ECategory category;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EEventStatus status;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    /* 연관 관계 속성 */

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
