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
@Schema(name = "B_ReviewByPlaceResponseDto", description = "특정 장소에 대한 리뷰 상세 DTO")
public class ReviewByPlaceIdResponseDto {

    @Schema(description = "리뷰 작성자 닉네임", example = "홍길동")
    private String author;

    @Schema(description = "리뷰 별점", example = "4")
    private int rating;

    @Schema(description = "리뷰 내용", example = "정말 멋진 경험이었어요!")
    private String content;

    @Schema(description = "작성 날짜", example = "2025-05-12")
    private String createdAt;
}
