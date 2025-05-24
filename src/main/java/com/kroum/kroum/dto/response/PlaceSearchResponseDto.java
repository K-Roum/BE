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
@Schema(name = "B_PlaceSearchResponseDto", description = "검색 결과 반환 DTO")
public class PlaceSearchResponseDto {

    @Schema(description = "위도", example = "37.5665")
    private double latitude;

    @Schema(description = "경도", example = "126.9780")
    private double longitude;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.kroum.com/places/gyungbok.jpg")
    private String firstImageUrl;

   /* @Schema(description = "추가 이미지 리스트", example = "[\"https://.../1.jpg\", \"https://.../2.jpg\"]")
    private List<String> imageUrls;*/

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
