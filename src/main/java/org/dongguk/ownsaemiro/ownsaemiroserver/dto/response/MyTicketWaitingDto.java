package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MyTicketWaitingDto(
        @JsonProperty("image")
        String image,

        @JsonProperty("title")
        String name,

        @JsonProperty("duration")
        String activatedAt,

        @JsonProperty("status")
        String status

) {
}
