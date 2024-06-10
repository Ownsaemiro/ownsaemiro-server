package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  알림 정보 속성  */

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String content;

    /*  연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notification(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
