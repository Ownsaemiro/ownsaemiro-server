package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserWallet;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyPointDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserWalletRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWalletService {
    private final UserWalletRepository userWalletRepository;
    public MyPointDto checkMyPoint(Long userWalletId){
        UserWallet userWallet = userWalletRepository.findById(userWalletId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_WALLET));

        return MyPointDto.builder()
                .point(userWallet.getPoint())
                .build();
    }
}
