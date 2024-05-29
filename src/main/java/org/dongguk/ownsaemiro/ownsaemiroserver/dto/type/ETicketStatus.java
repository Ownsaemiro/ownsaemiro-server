package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ETicketStatus {
    BEFORE("BEFORE"),
    OCCUPIED("OCCUPIED"),
    TRANSFER("TRANSFER");

    private final String status;
}
