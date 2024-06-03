package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum EProvider {
    DEFAULT("DEFAULT"),
    KAKAO("KAKAO"),
    NAVER("NAVER");
    private final String provider;

    public static EProvider toEnum(String provider){
        for (EProvider eProvider : EProvider.values()){
            if (eProvider.provider.equals(provider))
                return eProvider;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }

}
