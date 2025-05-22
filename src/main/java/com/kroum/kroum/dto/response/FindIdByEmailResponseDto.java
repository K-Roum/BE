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
@Schema(name = "B_FindIdByEmailResponseDto", description = "아이디 찾기 요청 응답 DTO")
public class FindIdByEmailResponseDto {

    @Schema(description = "로그인 ID", example = "loginId123")
    private String loginId;
}
