package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.TestAuthSignUpDto;
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
    @Builder
    public User(String serialId, String password, ERole role, EProvider provider) {
        this.serialId = serialId;
        this.password = password;
        this.role = role;
        this.provider = provider;
    }

    // 폼로그인 사용자 생성
    public static User signUp(TestAuthSignUpDto authSignUpDto, String encodedPassword, ERole role){
        User newUser = User.builder()
                .serialId(authSignUpDto.serialId())
                .password(encodedPassword)
                .provider(EProvider.DEFAULT)
                .role(role)
                .build();
        newUser.register(authSignUpDto.name(), authSignUpDto.nickname(), authSignUpDto.phoneNumber());
        return newUser;
    }
    // 폼로그인 사용자 정보 저장
    public void register(String name, String nickname, String phoneNumber){
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.isBanned = Boolean.FALSE;
    }
}
