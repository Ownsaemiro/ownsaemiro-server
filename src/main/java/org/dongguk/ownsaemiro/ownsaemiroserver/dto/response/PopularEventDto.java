package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PopularEventDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String name,

        @JsonProperty("image")
        String image,

        @JsonProperty("duration")
        String duration
) {
}