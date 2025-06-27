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
@Schema(name = "B_SearchImagePreviewResponseDto", description = "이미지 검색용 DTO")
public class PlaceImagePreviewResponseDto {

    @Schema(description = "해당 이미지에 매핑되어 있는 placeId")
    private Long placeId;

    @Schema(description = "이미지 url")
    private String imageUrl;

}
