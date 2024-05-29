package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum EEventStatus {
    BEFORE("BEFORE"),
    SELLING("SELLING"),
    SOLDOUT("SOLDOUT"),
    COMPLETE("COMPLETE"),
    PAUSE("PAUSE");

    private final String state;
}
