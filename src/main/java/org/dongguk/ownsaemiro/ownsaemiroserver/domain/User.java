package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EProvider;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*    사용자 인증 관련 정보    */

    private String serialId;
    private String password;
    @Enumerated(EnumType.STRING)
    private ERole role;
    @Enumerated(EnumType.STRING)
    private EProvider provider;

    /*    사용자 정보    */

    private String name;
    private String nickname;
    private String phoneNumber;
    private Boolean isBanned;



}
