package com.kroum.kroum.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "B_PlaceReviewsResponseDto", description = "선택한 장소에 대한 리뷰 리스트 응답 DTO")
public class PlaceReviewsResponseDto {

    @Schema(description = "리뷰 달린 갯수", example = "213")
    private Long totalCount;

    @Schema(description = "모든 리뷰의 평점", example = "3.7")
    private Double averageRating;

    @Schema(description = "리뷰 리스트")
    private List<PlaceReviewDto> placesReviews;

}
