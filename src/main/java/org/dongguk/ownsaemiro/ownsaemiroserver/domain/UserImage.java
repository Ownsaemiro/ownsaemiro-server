package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("USER")
@PrimaryKeyJoinColumn(
        foreignKey = @ForeignKey(name = "fk_user_image_id")
)
public class UserImage extends Image {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserImage(
            String url,
            LocalDate createdAt,
            User user
    ) {
        super(url, createdAt);

        this.user = user;
    }

}
