package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ECategory {
    MUSICAL("뮤지컬"),
    EXHIBITION("전시"),
    THEATER("연극"),
    CONCERT("콘서트"),
    SPORT("스포츠");

    private final String category;

    public static ECategory toECategory(String strCategory){
        for(ECategory eCategory : ECategory.values()){
            if (eCategory.category.equals(strCategory))
                return eCategory;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }
}
