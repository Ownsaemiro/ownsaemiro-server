package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.AuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EProvider;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@DynamicUpdate
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*    사용자 인증 관련 정보    */

    @Column(name = "serial_id", nullable = false)
    private String serialId;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ERole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private EProvider provider;

    /*    사용자 정보    */

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "is_banned", nullable = false)
    private Boolean isBanned;

    @Builder
    public User(String serialId, String password, ERole role, EProvider provider) {
        this.serialId = serialId;
        this.password = password;
        this.role = role;
        this.provider = provider;
    }

    // 사용자 생성
    public static User signUp(AuthSignUpDto authSignUpDto, String encodedPassword, EProvider provider, ERole role){
        User newUser = User.builder()
                .serialId(authSignUpDto.serialId())
                .password(encodedPassword)
                .provider(provider)
                .role(role)
                .build();
        newUser.register(authSignUpDto.name(), authSignUpDto.nickname(), authSignUpDto.phoneNumber());
        return newUser;
    }
    // 사용자 정보 저장
    public void register(String name, String nickname, String phoneNumber){
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.isBanned = Boolean.FALSE;
    }

    // 사용자 닉네임 변경
    public String updateNickname(String nickname){
        this.nickname = nickname;
        return this.nickname;
    }

    // 사용자 정지
    public Boolean ban(){
        this.isBanned = Boolean.TRUE;
        return true;
    }
}
