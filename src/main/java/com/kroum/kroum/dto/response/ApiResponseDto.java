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
@Schema(name = "B_ApiResponseDto", description = "공통 응답 DTO")
public class ApiResponseDto {

    @Schema(description = "요청 성공 여부", example = "true")
    private boolean success;

    @Schema(description = "응답 메시지", example = "회원가입이 완료되었습니다.")
    private String message;
}
