package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AllAboutEventDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String name,

        @JsonProperty("image")
        String image,

        @JsonProperty("category")
        String category,

        @JsonProperty("duration_time")
        String runningTime,

        @JsonProperty("rating")
        String rating,

        @JsonProperty("address")
        String address,

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("description")
        String description,

        @JsonProperty("date")
        String activatedAt,

        @JsonProperty("is_liked")
        Boolean isLiked
) {
}
