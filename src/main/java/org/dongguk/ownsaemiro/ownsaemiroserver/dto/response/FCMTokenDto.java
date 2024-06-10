package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FCMTokenDto (
        @JsonProperty("fcm_token")
        String fcmToken
){
}
