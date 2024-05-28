package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TestAuthSignUpDto(
        @JsonProperty("serial_id")
        String serialId,
        @JsonProperty("password")
        String password,
        @JsonProperty("name")
        String name,
        @JsonProperty("nickname")
        String nickname,
        @JsonProperty("phone_number")
        String phoneNumber,
        @JsonProperty("role")
        String role

) {
}
