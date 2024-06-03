package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record EventDto(
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("event_image")
        String image,

        @JsonProperty("name")
        String name,

        @JsonProperty("address")
        String address,

        @JsonProperty("duration")
        String duration
) {
}
