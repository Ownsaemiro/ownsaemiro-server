package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record AdminApplyEventDto(
        @JsonProperty("page_info")
        PageInfo pageInfo,

        @JsonProperty("event_request")
        List<EventRequestDto> eventRequestsDto
) {
}
