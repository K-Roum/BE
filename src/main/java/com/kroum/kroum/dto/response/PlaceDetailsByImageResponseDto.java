package com.kroum.kroum.dto.response;

import com.kroum.kroum.dto.PlaceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "B_PlaceDetailsByImageResponseDto", description = "사진 검색에서 상세 정보 뿌려주는 장소 정보 DTO")
public class PlaceDetailsByImageResponseDto {

    @Schema(description = "검색한 장소에 대한 정보")
    private PlaceDto placeDto;

    @Schema(description = "주변 장소 정보 리스트 이미지용", example = "창덕궁 정보, 뭐 주변 맛집 정보 등")
    private List<NearbyPlaceImageResponseDto> nearbyPlacesImageDto;
}
