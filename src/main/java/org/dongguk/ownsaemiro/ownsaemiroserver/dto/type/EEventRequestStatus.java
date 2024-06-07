package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum EEventRequestStatus {
    WAITING("WAITING"),
    REJECT("REJECT"),
    COMPLETE("COMPLETE");
    private final String status;

    public static EEventRequestStatus toEnum(String status){
        for(EEventRequestStatus eStatus : EEventRequestStatus.values()){
            if (eStatus.status.equals(status))
                return eStatus;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }
}
