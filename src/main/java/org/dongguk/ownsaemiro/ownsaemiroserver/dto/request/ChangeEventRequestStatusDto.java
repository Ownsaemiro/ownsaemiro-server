package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ChangeEventRequestStatusDto(
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("status")
        String status
) {
}
