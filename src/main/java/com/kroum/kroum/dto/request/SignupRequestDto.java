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
@Schema(name = "A_SignupRequestDto", description = "회원가입 요청 DTO")
public class SignupRequestDto {

    @Schema(description = "아이디", example = "id123")
    private String loginId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @Schema(description = "닉네임", example = "zx지존하대영zx")
    private String nickname;
}
