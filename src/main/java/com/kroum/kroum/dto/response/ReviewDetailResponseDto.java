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
@Schema(name = "B_ReviewDetailResponseDto", description = "리뷰 상세 조회 DTO")
public class ReviewDetailResponseDto {

    @Schema(description = "장소 이름", example = "경복궁")
    private String placeName;

    @Schema(description = "리뷰 평점", example = "3")
    private int averageRating;

    @Schema(description = "리뷰 내용", example = "너무너무 맛있었어요~!! 매운 맛에 기절할 뻔...")
    private String content;

    @Schema(description = "작성 날짜", example = "2025-05-12")
    private String createdAt;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.kroum.com/places/gyungbok.jpg")
    private String firstImageUrl;
}