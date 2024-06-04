package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MyTicketDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String name,

        @JsonProperty("image")
        String image,

        @JsonProperty("date")
        String activatedAt,

        @JsonProperty("purchased_date")
        String boughtAt,

        @JsonProperty("transaction_number")
        String orderId
) {
}
