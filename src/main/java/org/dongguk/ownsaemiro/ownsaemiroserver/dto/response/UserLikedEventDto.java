package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserLikedEventDto(
        @JsonProperty("liked_id")
        Long likedId,

        @JsonProperty("event_id")
        Long eventId,

        @JsonProperty("image")
        String url,

        @JsonProperty("name")
        String name,

        @JsonProperty("duration")
        String duration
) {
}
