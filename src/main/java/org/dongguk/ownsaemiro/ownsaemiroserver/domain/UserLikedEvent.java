package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_liked_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLikedEvent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Builder
    public UserLikedEvent(User user, Event event) {
        this.user = user;
        this.event = event;
    }
    // 사용자가 좋아요한 행사 저장
    public static UserLikedEvent create(User user, Event event){
        return UserLikedEvent.builder()
                .user(user)
                .event(event)
                .build();
    }
}
