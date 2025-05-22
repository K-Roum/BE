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
@Schema(name = "A_EmailVerificationRequestDto", description = "이메일 인증 요청 DTO")
public class EmailVerificationRequestDto {

    @Schema(description = "인증 받을 이메일", example = "user123@naver.com")
    private String email;
}
