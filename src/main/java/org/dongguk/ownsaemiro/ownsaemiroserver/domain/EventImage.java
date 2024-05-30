package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "event_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("EVENT")
@PrimaryKeyJoinColumn(
        foreignKey = @ForeignKey(name = "fk_event_image_id")
)
public class EventImage extends Image{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Builder
    public EventImage(
            String url,
            String name,
            LocalDate createdAt,
            Event event
    ) {
        super(url, name, createdAt);

        this.event = event;
    }

}
