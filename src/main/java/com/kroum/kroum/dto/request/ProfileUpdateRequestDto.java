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
@Schema(name = "A_ProfileUpdateRequestDto", description = "프로필 수정 요청 DTO")
public class ProfileUpdateRequestDto {
    // 이메일 변경 기능은 제외하는 걸로

    @Schema(description = "닉네임", example = "kroum123")
    private String nickname;

}
