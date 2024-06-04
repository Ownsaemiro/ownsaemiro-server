package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SellerOfEventDto(
        @JsonProperty("director")
        String nickname,

        @JsonProperty("duration_time")
        String runningTime,

        @JsonProperty("rating")
        String rating,

        @JsonProperty("address")
        String address

) {
}
