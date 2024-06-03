package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BanInfo(
        @JsonProperty("serial_id")
        String serialId,

        @JsonProperty("is_ban")
        Boolean ban
) {
}
