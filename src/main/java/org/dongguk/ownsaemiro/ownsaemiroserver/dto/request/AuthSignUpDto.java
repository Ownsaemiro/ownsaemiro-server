package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import net.minidev.json.JSONObject;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.AuthUtil;

@Builder
public record AuthSignUpDto(
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
