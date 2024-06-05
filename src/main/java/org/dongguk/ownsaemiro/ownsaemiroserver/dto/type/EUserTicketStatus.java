package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EUserTicketStatus {

    BEFORE_USE("BEFORE_USE"),

    AFTER_USE("AFTER_USE");

    private final String status;
}
