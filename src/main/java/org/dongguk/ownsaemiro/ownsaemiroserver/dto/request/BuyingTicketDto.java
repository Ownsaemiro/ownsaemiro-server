package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record BuyingTicketDto(
        @JsonProperty("date")
        LocalDate buyingDate
) {
}
