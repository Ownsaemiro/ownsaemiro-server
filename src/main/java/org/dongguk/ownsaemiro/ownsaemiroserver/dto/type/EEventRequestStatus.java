package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
        return null;
    }
}
