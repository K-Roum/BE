package com.kroum.kroum.controller;

import com.kroum.kroum.dto.response.SearchHistoryResponseDto;
import com.kroum.kroum.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Tag(name = "SearchHistory API", description = "최근 검색어를 가져오는 컨트롤러, 검색 창을 클릭 && 로그인이면 출력")
@RestController
@RequestMapping("/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @Operation(summary = "최근 검색어 조회 / 구현 완료", description = "로그인된 사용자의 최근 검색어 리스트를 반환")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SearchHistoryResponseDto.class))))
    @GetMapping
    public ResponseEntity<List<SearchHistoryResponseDto>> searchHistory(HttpSession session) {

        List<SearchHistoryResponseDto> historyList = searchHistoryService.getSearchHistories(session);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(historyList);
    }
}
