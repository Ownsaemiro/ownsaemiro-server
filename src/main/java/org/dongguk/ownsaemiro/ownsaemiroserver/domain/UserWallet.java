package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_wallets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWallet {
    @Id
    private Long id;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Builder
    public UserWallet(Long userId, Integer point) {
        this.id= userId;
        this.point = point;
    }

    /*  사용자 지갑 초기 생성  */
    public static UserWallet create(Long userId){
        return UserWallet.builder()
                .userId(userId)
                .point(0)
                .build();
    }

    public void pay(Integer amount){
        this.point -= amount;
    }
}
