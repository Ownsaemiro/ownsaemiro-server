package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangeSellingEventStatusDto(
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("status")
        String status
) {
}
