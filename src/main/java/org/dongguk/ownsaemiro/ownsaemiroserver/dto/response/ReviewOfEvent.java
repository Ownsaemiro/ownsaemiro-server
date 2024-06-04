package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ReviewOfEvent(
        @JsonProperty("id")
        Long id,

        @JsonProperty("nickname")
        String nickname,

        @JsonProperty("profile_image")
        String image,

        @JsonProperty("content")
        String content
) {
}
