package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AssignTicketDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("image")
        String image,

        @JsonProperty("title")
        String name,

        @JsonProperty("duration")
        String activatedAt
) {
}
