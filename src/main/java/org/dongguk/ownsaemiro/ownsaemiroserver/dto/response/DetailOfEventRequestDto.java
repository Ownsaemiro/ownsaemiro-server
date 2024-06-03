package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record DetailOfEventRequestDto(
        @JsonProperty("name")
        String name,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("running_time")
        Integer runningTime,

        @JsonProperty("address")
        String address,

        @JsonProperty("host_nickname")
        String hostNickname,

        @JsonProperty("description")
        String description
) {
}
