package com.kroum.kroum.controller;

import com.kroum.kroum.dto.request.EmailVerificationCodeRequestDto;
import com.kroum.kroum.dto.request.EmailVerificationRequestDto;
import com.kroum.kroum.dto.request.ReviewCreateRequestDto;
import com.kroum.kroum.dto.response.ApiResponseDto;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "EmailVerification API", description = "이메일 인증 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/email-verification")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "이메일 인증 발송 요청 / 구현 완료", description = "버튼을 누르면 이메일로 인증 코드 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일로 인증 코드 발송 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto> sendVerificationEmail(@RequestBody EmailVerificationRequestDto request) {
        emailVerificationService.sendVerificationEmail(request);

        return ResponseEntity.ok(new ApiResponseDto(true, "이메일로 인증 코드가 발송되었습니다."));
    }


    @Operation(summary = "인증 코드 확인 요청 / 구현 완료", description = "버튼을 누르면 내가 보낸 인증 코드와 클라이언트가 제출한 코드의 일치여부 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "인증 코드 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto> verifyCode(@RequestBody EmailVerificationCodeRequestDto request) {
        // 유효 기간 설정은 추후 구현 하든가 하자 지금 방식으로도 크게 뭐 문제는 없네..
        boolean validation = emailVerificationService.isVerificationCodeValid(request);

        if (!validation) return ResponseEntity.ok(new ApiResponseDto(false, "인증 실패"));

        return ResponseEntity.ok(new ApiResponseDto(true, "인증 완료"));
    }
}
