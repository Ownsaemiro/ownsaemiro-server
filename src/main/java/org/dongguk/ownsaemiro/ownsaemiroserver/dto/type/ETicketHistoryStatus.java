package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ETicketHistoryStatus {
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED"),
    TRANSFER("TRANSFER");
    private final String status;
}
