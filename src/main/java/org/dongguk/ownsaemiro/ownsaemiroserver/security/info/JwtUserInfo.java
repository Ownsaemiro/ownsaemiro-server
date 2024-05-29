package org.dongguk.ownsaemiro.ownsaemiroserver.security.info;

import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;

public record JwtUserInfo(Long userId, ERole role) {
}
