package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record DetailOfTicketDto(
        @JsonProperty("id")
        Long eventId,

        @JsonProperty("image")
        String image,

        @JsonProperty("title")
        String name,

        @JsonProperty("category")
        String category,

        @JsonProperty("duration_time")
        String runningTime,

        @JsonProperty("rating")
        String rating,

        @JsonProperty("address")
        String address,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("cell_phone_number")
        String phoneNumber,

        @JsonProperty("transaction_number")
        String orderId,

        @JsonProperty("buyer_id")
        Long buyerId,

        @JsonProperty("event_hash")
        String eventHash
) {
}
