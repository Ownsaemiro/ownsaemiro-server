package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketIdDto(
        @JsonProperty("ticket_id")
        Long ticketId
) {
}
