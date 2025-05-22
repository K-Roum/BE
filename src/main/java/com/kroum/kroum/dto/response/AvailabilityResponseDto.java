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
@Schema(name = "B_AvailabilityResponseDto", description = "중복 확인 응답 DTO(이메일, 닉네임, 로그인ID)")
public class AvailabilityResponseDto {

    @Schema(description = "사용 가능 여부", example = "true")
    private boolean available;
}
