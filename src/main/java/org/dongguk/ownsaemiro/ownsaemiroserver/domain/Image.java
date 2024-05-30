package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "images")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Image {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    public Image(
            String url,
            String name,
            LocalDate createdAt
    ) {
        this.url = url;
        this.name = name;
        this.createdAt = createdAt;
    }
}
