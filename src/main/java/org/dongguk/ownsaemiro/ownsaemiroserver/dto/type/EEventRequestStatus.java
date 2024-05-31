package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EEventRequestStatus {
    WAITING("승인대기"),
    REJECT("승인거절"),
    COMPLETE("승인완료");
    private final String status;

    public static EEventRequestStatus toEnum(String status){
        for(EEventRequestStatus eStatus : EEventRequestStatus.values()){
            if (eStatus.status.equals(status))
                return eStatus;
        }
        return null;
    }
}
