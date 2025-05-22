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
@Schema(name = "A_ReviewUpdateRequestDto", description = "리뷰 수정 요청 DTO")
public class ReviewUpdateRequestDto {

    @Schema(description = "수정할 별점, 1 ~ 5", example = "1")
    private int rating;

    @Schema(description = "수정할 리뷰 텍스트", example = "여기 진짜 ㄹㅇ 별로임")
    private String content;
}