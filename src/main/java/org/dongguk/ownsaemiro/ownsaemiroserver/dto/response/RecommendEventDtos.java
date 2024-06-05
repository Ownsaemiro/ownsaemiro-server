package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.SelfValidating;

import java.util.List;

@Getter
public class RecommendEventDtos extends SelfValidating<RecommendEventDtos> {

    @JsonProperty("events")
    @NotNull(message = "이벤트는 null이 될 수 없습니다.")
    private final List<RecommendEventDto> events;

    @Builder
    public RecommendEventDtos(List<RecommendEventDto> events) {
        this.events = events;
        this.validateSelf();
    }

    public static class RecommendEventDto {

        @JsonProperty("id")
        @NotNull(message = "이벤트 ID는 null이 될 수 없습니다.")
        private final Long id;

        @JsonProperty("title")
        @NotNull(message = "이벤트 제목은 null이 될 수 없습니다.")
        private final String title;

        @JsonProperty("image")
        @NotNull(message = "이벤트 이미지는 null이 될 수 없습니다.")
        private final String image;

        @JsonProperty("duration")
        @NotNull(message = "이벤트 기간은 null이 될 수 없습니다.")
        private final String duration;

        @JsonProperty("address")
        @NotNull(message = "이벤트 주소는 null이 될 수 없습니다.")
        private final String address;

        @Builder
        public RecommendEventDto(Long id, String title, String image, String duration, String address) {
            this.id = id;
            this.title = title;
            this.image = image;
            this.duration = duration;
            this.address = address;
        }
    }
}
