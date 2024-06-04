package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ECategory {
    MUSICAL("MUSICAL"),
    EXHIBITION("EXHIBITION"),
    THEATER("THEATER"),
    CONCERT("CONCERT"),
    SPORT("SPORT");

    private final String category;

    public static ECategory toECategory(String strCategory){
        for(ECategory eCategory : ECategory.values()){
            if (eCategory.category.equals(strCategory))
                return eCategory;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }
    public static ECategory filterCondition(String category){
        for(ECategory eCategory : ECategory.values()){
            if (eCategory.category.equals(category))
                return eCategory;
        }
        return null;
    }
}
