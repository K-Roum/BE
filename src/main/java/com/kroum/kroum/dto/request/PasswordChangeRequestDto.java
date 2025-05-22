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
@Schema(name = "A_PasswordChangeRequestDto", description = "비밀번호 변경 요청 DTO")
public class PasswordChangeRequestDto {

    @Schema(description = "기존 비밀번호", example = "currentPassword123")
    // 암호화 필수
    private String currentPassword;

    @Schema(description = "변경할 비밀번호", example = "newPassword123")
    // 암호화 필수 2
    private String newPassword;

}
