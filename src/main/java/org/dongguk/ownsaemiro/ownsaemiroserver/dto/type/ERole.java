package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ERole {
    GUEST("GUEST", "ROLE_GUEST"),
    USER("USER", "ROLE_USER"),
    SELLER("SELLER", "ROLE_SELLER"),
    ADMIN("ADMIN", "ROLE_ADMIN");
    private final String role;
    private final String securityRole;
    public static ERole toERole(String strRole){
        for(ERole eRole : ERole.values()){
            if (eRole.role.equals(strRole))
                return eRole;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }

}
