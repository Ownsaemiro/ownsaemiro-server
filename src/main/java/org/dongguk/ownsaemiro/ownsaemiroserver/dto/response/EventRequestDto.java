package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record EventRequestDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name")
        String name,

        @JsonProperty("host_name")
        String hostName,

        @JsonProperty("apply_date")
        String applyDate,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("state")
        String state
) {
}
