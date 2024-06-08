package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WriteReviewOfEvent(
        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content
) {
}
