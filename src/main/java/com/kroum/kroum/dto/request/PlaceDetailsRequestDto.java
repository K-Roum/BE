package com.kroum.kroum.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "A_PlaceDetailsRequestDto", description = "특정 관광지 상세 정보 요청 dto이나 필요할 것 같지 않다. requestParam으로 받아서 처리 예정")
public class PlaceDetailsRequestDto {

    @Schema(description = "장소 id", example = "123")
    private Long placeId;

    @Schema(description = "사용자가 설정한 언어의 코드", example = "KO, korean 등 프론트단에서 들고 있는 대로")
    private String languageCode;
}
