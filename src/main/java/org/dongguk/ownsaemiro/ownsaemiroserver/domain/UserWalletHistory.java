package org.dongguk.ownsaemiro.ownsaemiroserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_wallet_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWalletHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  사용자 지갑 구매 내역 기본 속성  */
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    /*  연관 관계 속성  */
    @ManyToOne
    @JoinColumn(name = "user_wallet_id", nullable = false)
    private UserWallet userWallet;

    @Builder
    public UserWalletHistory(Integer amount, LocalDate createdAt, UserWallet userWallet) {
        this.amount = amount;
        this.createdAt = createdAt;
        this.userWallet = userWallet;
    }
}
