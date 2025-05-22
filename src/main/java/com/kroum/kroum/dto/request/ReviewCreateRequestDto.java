package com.kroum.kroum.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "A_ReviewCreateRequestDto", description = "리뷰 작성 요청 DTO")
public class ReviewCreateRequestDto {

    @Schema(description = "별점, 1 ~ 5 사이의 정수", example = "3")
    private double rating;

    @Schema(description = "리뷰 텍스트", example = "여기 진짜 ㄹㅇ 좋아요")
    private String content;
}
