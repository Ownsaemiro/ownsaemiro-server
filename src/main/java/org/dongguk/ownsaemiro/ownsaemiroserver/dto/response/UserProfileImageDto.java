package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.SelfValidating;

@Getter
public class UserProfileImageDto extends SelfValidating<UserProfileImageDto> {

    @JsonProperty("profile_image")
    @NotNull(message = "프로필 이미지는 필수값입니다.")
    private final String profileImage;

    @Builder
    public UserProfileImageDto(String profileImage) {
        this.profileImage = profileImage;
        this.validateSelf();
    }
}
