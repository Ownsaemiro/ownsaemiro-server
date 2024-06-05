package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record DetailInfoOfEventDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String name,

        @JsonProperty("image")
        String url,

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

        @JsonProperty("price")
        Integer price,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("is_liked")
        Boolean isLiked,

        @JsonProperty("remaining_seats")
        Long remainingSeats
) {
}
