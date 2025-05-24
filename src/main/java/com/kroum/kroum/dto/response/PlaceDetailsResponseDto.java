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
@Schema(name = "B_PlaceDetailsResponseDto", description = "특정 관광지 상세 정보 응답 DTO")
public class PlaceDetailsResponseDto {

    @Schema(description = "리뷰 정보")
    private PlaceReviewsResponseDto reviews;

    @Schema(description = "찜 정보")
    private PlaceBookmarkDto bookmark;
}

