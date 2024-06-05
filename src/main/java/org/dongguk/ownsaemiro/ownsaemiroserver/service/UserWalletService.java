package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserWallet;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyPointDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWalletService {
    private final UserWalletRepository userWalletRepository;

    /**
     * 사용자 포인트 조회하기
     */
    public MyPointDto checkMyPoint(Long userWalletId){
        UserWallet userWallet = userWalletRepository.findById(userWalletId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_WALLET));

        return MyPointDto.builder()
                .point(userWallet.getPoint())
                .build();
    }

    /**
     * 사용자 포인트 충전하기
     */
    @Transactional
    public MyPointDto rechargePoint(Long userWalletId, MyPointDto rechargePointDto){
        UserWallet userWallet = userWalletRepository.findById(userWalletId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_WALLET));

        // 포인트 충전
        Integer myPoint = userWallet.recharge(rechargePointDto.point());


        return MyPointDto.builder()
                .point(myPoint)
                .build();
    }
}
