package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchEventsDto(
        @JsonProperty("page_info")
        PageInfo pageInfo,

        @JsonProperty("events")
        List<SearchEventDto> searchEventDto
) {

}
