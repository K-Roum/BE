package com.kroum.kroum.dto.response;

import com.kroum.kroum.dto.PlaceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "B_NearbyPlaceImageResponseDto", description = "이미지로 인근 장소 검색하는 DTO")
public class NearbyPlaceImageResponseDto {

    @Schema(description = "장소 정보")
    private PlaceDto place;

    @Schema(description = "거리", example = "1.5")
    private Double distance;



}
