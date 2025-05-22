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
@Schema(name = "A_PasswordResetRequestDto", description = "비밀번호 초기화 요청 DTO")
public class PasswordResetRequestDto {

    // 아이디와 이메일을 알려주면 리셋한 임시 비밀번호를 이메일로 보내준다
    @Schema(description = "로그인에 사용하는 id", example = "user123")
    private String loginId;

    @Schema(description = "비밀번호 찾기에 사용될 기반 이메일", example = "userTest@gmail.com")
    private String email;

}
