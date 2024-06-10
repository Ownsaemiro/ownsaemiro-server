package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationIdDto (
        @JsonProperty("id")
        Long notificationId
){
}
