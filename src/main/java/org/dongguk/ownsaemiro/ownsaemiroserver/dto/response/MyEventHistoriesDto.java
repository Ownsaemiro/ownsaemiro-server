package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record MyEventHistoriesDto(
        @JsonProperty("page_info")
        PageInfo pageInfo,

        @JsonProperty("my_event_histories")
        List<EventHistoryDto> eventHistoriesDto
) {
}
