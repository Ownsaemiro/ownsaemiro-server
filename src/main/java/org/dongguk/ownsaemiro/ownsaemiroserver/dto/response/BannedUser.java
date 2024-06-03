package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BannedUser(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("serial_id")
        String serialId,

        @JsonProperty("role")
        String role,

        @JsonProperty("provider")
        String provider,

        @JsonProperty("ban")
        Boolean ban
) {
}
