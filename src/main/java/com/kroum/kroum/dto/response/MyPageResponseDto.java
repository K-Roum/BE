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
@Schema(name = "B_MyPageResponseDto", description = "마이페이지 통합 응답 DTO")
public class MyPageResponseDto {

    @Schema(description = "유저 프로필 정보")
    private ProfileResponseDto profile;

    @Schema(description = "유저가 찜한 장소 목록")
    private List<BookmarkResponseDto> bookmarks;

    @Schema(description = "유저가 작성한 리뷰 목록")
    private List<ReviewSummaryResponseDto> reviews;

}

