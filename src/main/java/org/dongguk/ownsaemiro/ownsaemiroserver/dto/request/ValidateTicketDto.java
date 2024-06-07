package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateTicketDto(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("ticket_id")
        Long ticketId,

        @JsonProperty("event_hash")
        String eventHash,

        @JsonProperty("device_id")
        String deviceId
) {
}
