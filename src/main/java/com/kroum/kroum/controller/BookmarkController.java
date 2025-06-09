package com.kroum.kroum.controller;

import com.kroum.kroum.dto.response.ApiResponseDto;
import com.kroum.kroum.dto.response.BookmarkResponseDto;
import com.kroum.kroum.dto.response.PlaceBookmarkDto;
import com.kroum.kroum.service.BookmarkService;
import com.kroum.kroum.util.SessionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bookmark API", description = "북마크 관련해서 다루는 컨트롤러")
@RestController
@RequestMapping("/bookmarks")
@Slf4j
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "찜 추가 / 구현 완료", description = "버튼을 누르면 찜이 추가됨")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 찜이 추가됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{placeId}")
    public ResponseEntity<PlaceBookmarkDto> addBookmark(@PathVariable Long placeId, HttpSession session) {
        bookmarkService.addBookmark(placeId, session);

        PlaceBookmarkDto response = bookmarkService.getBookmark(placeId, session);
        log.info("북마크 추가 컨트롤러 요청");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "찜 삭제 / 구현 완료", description = "찜이 된 버튼을 누르면 찜이 삭제됨")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 찜이 삭제됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{placeId}")
    public ResponseEntity<PlaceBookmarkDto> deleteBookmark(@PathVariable Long placeId, HttpSession session) {

        bookmarkService.deleteBookmark(placeId, session);

        PlaceBookmarkDto response = bookmarkService.getBookmark(placeId, session);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "요약된 찜 목록 가져오기 / 구현 완료", description = "마이페이지를 누를 떄 내부 API로 사용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 찜 목록이 호출됨",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookmarkResponseDto.class)))), // 이거 ArraySchema 리턴 하도록
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<BookmarkResponseDto>> getBookmarks(HttpSession session) {

        List<BookmarkResponseDto> response = bookmarkService.getBookmarks(session);

        return ResponseEntity.ok(response);
    }
}
