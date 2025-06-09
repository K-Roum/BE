package com.kroum.kroum.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "B_PlaceReviewDto", description = "해당 장소에 대한 리뷰 단건 조회 DTO")
public class PlaceReviewDto {

    @Schema(description = "닉네임", example = "둘리는공룡")
    private String nickName;

    @Schema(description = "리뷰 내용", example = "맛있어요!")
    private String content;

    @Schema(description = "별점", example = "3")
    private int rating;

    @Schema(description = "작성 일시", example = "2025-05-22T18:46:15")
    private LocalDateTime createdAt;

}
