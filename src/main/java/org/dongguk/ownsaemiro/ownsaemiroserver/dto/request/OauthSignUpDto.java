package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OauthSignUpDto(
        @JsonProperty("serial_id")
        String serialId,

        @JsonProperty("device_id")
        String deviceId,

        @JsonProperty("fcm_token")
        String fcmToken,

        @JsonProperty("name")
        String name,

        @JsonProperty("nickname")
        String nickname,

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("provider")
        String provider

) {
}
