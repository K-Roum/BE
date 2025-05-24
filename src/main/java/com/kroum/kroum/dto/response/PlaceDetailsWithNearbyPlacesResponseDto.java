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
@Schema(name = "B_PlaceDetailsWithNearbyPlacesResponseDto", description = "특정 관광지 상세 정보 및 주변 장소 정보 요청 반환 DTO")
public class PlaceDetailsWithNearbyPlacesResponseDto {

    @Schema(description = "상세 정보 객체", example = "경복궁을 선택하면 경복궁에 관한 정보")
    private PlaceDetailsResponseDto details;

    // 이거는 3개 반환해주는게 좋을 것 같음
    @Schema(description = "주변 장소 정보 리스트", example = "창덕궁 정보, 뭐 주변 맛집 정보 등")
    private List<NearbyPlaceResponseDto> recommendations;
}
