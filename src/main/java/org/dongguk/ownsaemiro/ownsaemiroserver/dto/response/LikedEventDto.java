package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LikedEventDto(
        @JsonProperty("liked_id")
        Long likedId,

        @JsonProperty("event_id")
        Long eventId,

        @JsonProperty("is_liked")
        Boolean isLiked
) {
}
