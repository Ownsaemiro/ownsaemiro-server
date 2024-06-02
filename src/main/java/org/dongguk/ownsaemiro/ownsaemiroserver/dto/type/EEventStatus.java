package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum EEventStatus {
    BEFORE("BEFORE"),
    SELLING("SELLING"),
    SOLDOUT("SOLDOUT"),
    COMPLETE("COMPLETE"),
    PAUSE("PAUSE");

    private final String state;

    public static EEventStatus toEnum(String strStatus){
        for (EEventStatus eStatus : EEventStatus.values()){
            if (eStatus.getState().equals(strStatus))
                return eStatus;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }
}
