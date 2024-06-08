package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ParticipatedEventDto(
        @JsonProperty("id")
        Long eventId,

        @JsonProperty("image")
        String image,

        @JsonProperty("title")
        String name,

        @JsonProperty("activated_at")
        String activatedAt,

        @JsonProperty("bought_at")
        String boughtAt,

        @JsonProperty("order_id")
        String orderId


) {
}
