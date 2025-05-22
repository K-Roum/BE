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
@Schema(name = "B_ProfileResponseDto", description = "프로필 조회 요청 DTO")
public class ProfileResponseDto {

    @Schema(description = "닉네임", example = "대영123")
    private String nickname;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;
}
