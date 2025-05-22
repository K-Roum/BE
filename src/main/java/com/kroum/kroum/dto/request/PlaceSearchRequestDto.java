package com.kroum.kroum.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "A_PlaceSearchRequestDto", description = "장소 검색 요청 DTO")
public class PlaceSearchRequestDto {

    @Schema(description = "검색 문장 쿼리", example = "서울의 떡볶이 맛집을 추천해줘")
    private String query;

    @Schema(description = "사용자가 설정한 언어의 코드", example = "KO, korean 등 프론트단에서 들고 있는 대로")
    private String languageCode;
}
