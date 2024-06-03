package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UnlikedEventDto(
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("is_liked")
        Boolean isLiked
) {
}
