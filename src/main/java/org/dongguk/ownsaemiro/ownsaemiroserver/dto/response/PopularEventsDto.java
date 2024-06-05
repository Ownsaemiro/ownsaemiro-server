package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record PopularEventsDto(
        @JsonProperty("events")
        List<PopularEventDto> eventsDto
) {

}