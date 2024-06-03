package org.dongguk.ownsaemiro.ownsaemiroserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BanSerialId(
        @JsonProperty("serial_id")
        String serialId
) {
}
