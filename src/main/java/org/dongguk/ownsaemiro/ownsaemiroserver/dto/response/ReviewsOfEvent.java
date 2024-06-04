package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ReviewsOfEvent(
        @JsonProperty("reviews")
        List<ReviewOfEvent> reviewsDto
) {
}
