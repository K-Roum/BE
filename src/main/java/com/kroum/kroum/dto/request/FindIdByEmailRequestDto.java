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
@Schema(name ="A_FindIdByEmailRequestDto", description = "이메일 기반 아이디 찾기 요청 DTO")
public class FindIdByEmailRequestDto {

    // 이메일만 넘겨주면 이메일이 unique하므로 DB에서 아이디를 찾아올 수 있다
    @Schema(description = "아이디 찾기에 사용될 기반 이메일", example = "userTest@gmail.com")
    private String email;
}
