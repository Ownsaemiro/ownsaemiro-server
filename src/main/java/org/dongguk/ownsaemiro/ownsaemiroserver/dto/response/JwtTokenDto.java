package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import lombok.Builder;

import java.io.Serializable;
@Builder
public record JwtTokenDto(String accessToken, String refreshToken) implements Serializable {
    public static JwtTokenDto of (String accessToken, String refreshToken){
        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
