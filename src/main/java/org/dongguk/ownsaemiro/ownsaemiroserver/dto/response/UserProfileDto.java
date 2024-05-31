package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProfileDto(
        @JsonProperty("image")
        String url,

        @JsonProperty("nickname")
        String nickname
) {
}
