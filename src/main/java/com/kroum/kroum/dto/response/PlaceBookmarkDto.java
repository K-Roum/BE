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
@Schema(name = "B_PlaceBookmarkDto", description = "찜 정보 DTO")
public class PlaceBookmarkDto {

    @Schema(description = "찜한 사람 수", example = "32")
    private int bookmarkCount;

    @Schema(description = "현재 사용자가 찜했는지 여부", example = "true")
    private boolean bookmarked;
}

