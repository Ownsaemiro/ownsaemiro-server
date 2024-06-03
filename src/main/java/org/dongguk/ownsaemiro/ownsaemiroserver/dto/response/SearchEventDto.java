package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SearchEventDto(
        @JsonProperty("event_id")
        Long eventId,

        @JsonProperty("event_image")
        String url,

        @JsonProperty("name")
        String name,

        @JsonProperty("address")
        String address,

        @JsonProperty("duration")
        String duration
) {
}
