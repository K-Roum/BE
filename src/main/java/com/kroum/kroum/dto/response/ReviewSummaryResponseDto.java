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
@Schema(name = "B_ReviewSummaryResponseDto", description = "마이페이지용 리뷰 요약 DTO")
public class ReviewSummaryResponseDto {

    @Schema(description = "장소 ID", example = "123")
    private Long placeId;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.kroum.com/places/gyungbok.jpg")
    private String firstImageUrl; // UI에서 썸네일 표시 가능

    @Schema(description = "리뷰 평점", example = "4")
    private int rating;

    @Schema(description = "장소 이름", example = "경복궁")
    private String placeName;

}
