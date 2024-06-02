package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record EventHistoryDto(
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("event_name")
        String name,

        @JsonProperty("host_name")
        String hostNickname,

        @JsonProperty("apply_date")
        String applyDate,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("seat")
        Integer seat,

        @JsonProperty("status")
        String status
) {
}
