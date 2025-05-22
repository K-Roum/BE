package com.kroum.kroum.exception;

import com.kroum.kroum.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 익셉션핸들러 클래스
 * 각 클래스에서 익셉션 구현해놓고 여기서 호출해서 사용하는 방식
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequestException(InvalidRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(exception.getMessage(), 400));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponseDto> handleInternalServerException(InternalServerException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(exception.getMessage(), 500));
    }

}