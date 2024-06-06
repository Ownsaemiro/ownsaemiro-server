package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.SelfValidating;

@Getter
public class UserNicknameDto extends SelfValidating<UserNicknameDto> {

    @JsonProperty("nickname")
    @NotNull(message = "닉네임은 필수값입니다.")
    private final String nickname;

    @Builder
    public UserNicknameDto(String nickname) {
        this.nickname = nickname;
        validateSelf();
    }
}
