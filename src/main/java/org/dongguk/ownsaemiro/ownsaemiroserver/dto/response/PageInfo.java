package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageInfo(
        @JsonProperty("total_page")
        Integer totalPage,
        @JsonProperty("current_page")
        Integer currentPage,
        @JsonProperty("total_cnt")
        Long totalCnt,
        @JsonProperty("current_cnt")
        Integer currentCnt
) {
        public static PageInfo convert(Page<?> lists, Integer page){
                return PageInfo.builder()
                        .totalPage(lists.getTotalPages())
                        .currentPage(page)
                        .totalCnt(lists.getTotalElements())
                        .currentCnt(lists.getNumberOfElements())
                        .build();
        }
}
