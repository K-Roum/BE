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
@Schema(name = "B_BookmarkResponseDto", description = "북마크 응답 DTO")
public class BookmarkResponseDto {

    @Schema(description = "장소 ID", example = "123")
    private Long placeId;

    @Schema(description = "장소 이름", example = "경복궁")
    private String placeName;

    @Schema(description = "찜한 날짜", example = "2025-05-12")
    private String createdAt;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.kroum.com/places/gyungbok.jpg")
    private String firstImageUrl;

}
