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
@Schema(name = "B_NearbyPlaceResponseDto", description = "주변 장소 정보 요청 반환 DTO")
public class NearbyPlaceResponseDto {

    @Schema(description = "장소 정보")
    private PlaceSearchResponseDto place;

    @Schema(description = "거리", example = "1.5")
    private Double distance;

}
