package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateTicketDto(
        @JsonProperty("event_id")
        Long eventId
) {
}
