package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;

@Entity
@Getter
@Table(name = "tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hash", nullable = false, length = 512)
    private String hash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ETicketStatus status;

}
