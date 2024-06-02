package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AvailableSerialIdDto(
        @JsonProperty("available")
        Boolean available
) {
}
