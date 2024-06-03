package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ChangedEventRequestStatusDto (
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("status")
        String status

){
}
