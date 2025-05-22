package com.kroum.kroum.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "B_SearchHistoryResponseDto", description = "검색 히스토리 응답 DTO")
public class SearchHistoryResponseDto {

    @Schema(description = "검색어", example = "서울 야경 명소")
    private String searchText;

    @Schema(description = "검색한 날짜와 시간", example = "2025-05-10 22:31:15")
    private String createdAt;
}
