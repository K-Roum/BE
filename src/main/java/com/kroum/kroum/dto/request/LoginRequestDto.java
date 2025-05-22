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
@Schema(name = "A_LoginRequestDto", description = "로그인 요청 DTO")
public class LoginRequestDto {

    @Schema(description = "유저 email", example = "abc123@naver.com")
    private String email;

    @Schema(description = "유저 비밀번호", example = "password123")
    private String password;

}
