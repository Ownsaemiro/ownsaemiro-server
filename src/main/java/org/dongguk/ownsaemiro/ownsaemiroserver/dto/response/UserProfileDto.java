package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserProfileDto(
        @JsonProperty("image")
        String url,

        @JsonProperty("nickname")
        String nickname
) {
}
