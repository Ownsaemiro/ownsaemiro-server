package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WriteReviewOfEvent(
        @JsonProperty("content")
        String content
) {
}
