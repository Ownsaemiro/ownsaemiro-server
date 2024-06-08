package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.WriteReviewOfEvent;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "event_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventReview {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  행사 리뷰 속성  */

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 3000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    /*  연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Builder
    public EventReview(String title, String content, User user, Event event) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDate.now();
        this.user = user;
        this.event = event;
    }

    public static EventReview create(User user, Event event, WriteReviewOfEvent writeReviewOfEvent){
        return EventReview.builder()
                .title(writeReviewOfEvent.title())
                .content(writeReviewOfEvent.content())
                .user(user)
                .event(event)
                .build();
    }
}
