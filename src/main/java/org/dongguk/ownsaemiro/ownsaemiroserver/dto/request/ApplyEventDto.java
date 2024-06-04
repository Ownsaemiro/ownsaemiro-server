package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApplyEventDto(
        @JsonProperty("event_name")
        String name,

        @JsonProperty("start_date")
        String startDate,

        @JsonProperty("end_date")
        String endDate,

        @JsonProperty("seat_cnt")
        Integer seats,

        @JsonProperty("price")
        Integer price,

        @JsonProperty("running_time")
        Integer runningTime,

        @JsonProperty("address")
        String address,

        @JsonProperty("category")
        String category,

        @JsonProperty("description")
        String description,

        @JsonProperty("host_name")
        String hostName,

        @JsonProperty("rating")
        String rating
) {
}
