package com.kroum.kroum.dto;

import com.kroum.kroum.dto.response.PlaceDetailsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "B_PlaceDto", description = "해당 장소에 대한 기본 장소 DTO")
public class PlaceDto {
    // 이미지 정보 + 리뷰 정보 + 찜 정보

    @Schema(description = "리뷰 정보, 찜 정보 등등")
    private PlaceDetailsResponseDto placeDetailsResponseDto;

    @Schema(description = "위도", example = "37.5665")
    private double latitude;

    @Schema(description = "경도", example = "126.9780")
    private double longitude;

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
