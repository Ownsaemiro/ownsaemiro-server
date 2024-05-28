package org.dongguk.ownsaemiro.ownsaemiroserver.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ERole {
    USER("USER", "ROLE_USER"),
    SELLER("SELLR", "ROLE_SELLER"),
    ADMIN("ADMIN", "ROLE_ADMIN");
    private final String role;
    private final String securityRole;

}
