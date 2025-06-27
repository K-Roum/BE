package com.kroum.kroum.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "B_PlaceDetailsByPlaceIdResponseDto", description = "마이페이지에서 찜, 리뷰 카드 누를 때 쓰는 DTO")
public class PlaceDetailsByPlaceIdResponseDto {

    @Schema(description = "해당 장소에 대한 찜, 리뷰 정보")
    private PlaceDetailsResponseDto placeDetails;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.kroum.com/places/gyungbok.jpg")
    private String firstImageUrl;

    @Schema(description = "장소 이름", example = "경복궁")
    private String placeName;

    @Schema(description = "장소 설명", example = "조선시대 궁궐, 서울의 대표 관광지")
    private String description;

    @Schema(description = "주소", example = "서울 종로구 사직로 161")
    private String address;

    @Schema(description = "찜 여부", example = "true")
    private boolean bookmarked;

    @Schema(description = "장소 id", example = "123456")
    private Long placeId;





}
