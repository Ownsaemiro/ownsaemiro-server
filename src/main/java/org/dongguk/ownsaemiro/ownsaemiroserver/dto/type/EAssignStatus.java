package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EAssignStatus {

    WAITING("대기"),

    FAIL("낙첨"),

    SUCCESS("수령 대기중");

    private final String status;
}
