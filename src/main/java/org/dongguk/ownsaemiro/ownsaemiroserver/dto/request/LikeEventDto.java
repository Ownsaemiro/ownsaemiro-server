package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LikeEventDto(
        @JsonProperty("event_id")
        Long id
) {
}
