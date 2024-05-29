package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ECategory {
    MUSICAL("뮤지컬"),
    EXHIBITION("전시"),
    THEATER("연극"),
    CONCERT("콘서트"),
    SPORT("스포츠");

    private final String category;
}
