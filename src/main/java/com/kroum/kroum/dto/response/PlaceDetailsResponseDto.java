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
@Schema(name = "B_PlaceDetailsResponseDto", description = "특정 관광지 상세 정보 요청 반환 dto")
public class PlaceDetailsResponseDto {
    // 기본 정보 넘겨 준거는 캐시로 프론트단에서 들고 있고 백엔드단에서는 필요한 정보만 추가로 넘겨준다
    // 만약 다 넘겨 줄시에는 속도에서 오버헤드 난다!
    // 리뷰 갯수, 리뷰 평점, 북마크 갯수, 북마크 여부

    @Schema(description = "리뷰 갯수", example = "41")
    private int reviewCount;

    @Schema(description = "리뷰 평균 평점", example = "4.5")
    private double averageRating; // DB에서 값 갖고 올 떄 계산 후 소수점 자리 처리 해주기!

    @Schema(description = "찜 갯수", example = "32")
    private int bookmarkCount;

    @Schema(description = "찜 여부, 로그인 유저 한정", example = "true")
    private boolean bookmarked;
}
