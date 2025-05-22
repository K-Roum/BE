package com.kroum.kroum.controller;

import com.kroum.kroum.dto.response.ApiResponseDto;
import com.kroum.kroum.dto.response.BookmarkResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bookmark API", description = "북마크 관련해서 다루는 컨트롤러")
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    @Operation(summary = "찜 추가", description = "버튼을 누르면 찜이 추가됨")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 찜이 추가됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{placeId}")
    public ResponseEntity<ApiResponseDto> addBookmark(@PathVariable Long placeId, HttpSession session) {
        // 찜 추가를 누르면 북마크 테이블에 bookmarked가 false -> true로 바뀌어야 함
        return ResponseEntity.ok(new ApiResponseDto(true, "찜 목록에 추가되었습니다."));
    }

    @Operation(summary = "찜 삭제", description = "찜이 된 버튼을 누르면 찜이 삭제됨")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 찜이 삭제됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponseDto> deleteBookmark(@PathVariable Long placeId, HttpSession session) {
        // 찜 추가를 누르면 북마크 테이블에 bookmarked가 false -> true로 바뀌어야 함
        return ResponseEntity.ok(new ApiResponseDto(true, "찜 목록에서 삭제되었습니다."));
    }

    @Operation(summary = "요약된 찜 목록 가져오기", description = "마이페이지를 누를 떄 내부 API로 사용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 찜 목록이 호출됨",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookmarkResponseDto.class)))), // 이거 ArraySchema 리턴 하도록
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<BookmarkResponseDto>> getBookmarks(HttpSession session) {
        // 서비스 로직 구현
        List<BookmarkResponseDto> bookmarkResponseDto = List.of(new BookmarkResponseDto(123L, "경복궁", "2025-05-12", "https://cdn.kroum.com/places/gyungbok.jpg"),
                new BookmarkResponseDto(456L, "경복궁", "2025-05-12", "https://cdn.kroum.com/places/gyungbok.jpg"));
        return ResponseEntity.ok(bookmarkResponseDto);
    }
}
