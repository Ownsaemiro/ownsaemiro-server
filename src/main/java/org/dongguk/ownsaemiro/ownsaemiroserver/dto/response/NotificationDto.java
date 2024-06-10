package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record NotificationDto (
        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String title,

        @JsonProperty("body")
        String body
){
}
