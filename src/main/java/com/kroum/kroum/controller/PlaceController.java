package com.kroum.kroum.controller;

import com.kroum.kroum.dto.request.PlaceSearchRequestDto;
import com.kroum.kroum.dto.request.SaveSearchHistoryRequestDto;
import com.kroum.kroum.dto.response.*;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.service.PlaceService;
import com.kroum.kroum.service.SearchHistoryService;
import com.kroum.kroum.util.SearchInputAnalyzer;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Place API", description = "장소 검색, 검색 결과 등 제공 해주는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final SearchHistoryService searchHistoryService;
    private final SearchInputAnalyzer searchInputAnalyzer;

    @Operation(summary = "관광지 검색 / 구현완료", description = "문장형으로 관광지를 검색함")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 결과 리스트 성공적으로 받음",
                    content = @Content
                            (array = @ArraySchema(schema = @Schema(implementation = PlaceSearchResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "InvalidRequestException : 잘못된 요청 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerException : 내부 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/search")
    public ResponseEntity<List<PlaceSearchResponseDto>> searchPlace(@RequestBody PlaceSearchRequestDto request,
                                                                    HttpSession session) {
        String rawQuery = request.getQuery();
        String sanitized = placeService.preprocessSearchQuery(rawQuery);

        List<ContentIdDto> ids = placeService.getRecommendedPlaceIds(new PlaceSearchRequestDto(sanitized, request.getLanguageCode()));
        List<PlaceSearchResponseDto> places = placeService.getPlacesByIds(ids, session);
        Long userId = SessionUtil.getLoginUserId(session);

        if (userId != null) {
            searchHistoryService.save(
                    SessionUtil.getLoginUserId(session),
                    new SaveSearchHistoryRequestDto(request.getQuery())
            );
        }

        return ResponseEntity.ok(places);

    }

    @Operation(summary = "장소 상세 통합 요청 / 구현완료", description = "장소 클릭 시 상세 정보 + 리뷰 + 주변 장소 리스트 반환")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공 시 통합 상세 정보 반환",
                    content = @Content
                            (schema = @Schema(implementation = PlaceDetailsWithNearbyPlacesResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "InvalidRequestException : 잘못된 요청 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "InternalServerException : 내부 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{placeId}/with-everything")
    public ResponseEntity<PlaceDetailsWithNearbyPlacesResponseDto> getPlaceDetailsWithEverything(
            @PathVariable Long placeId,
            @RequestParam String languageCode,
            HttpSession session
    )
    {
        PlaceDetailsWithNearbyPlacesResponseDto response =
                placeService.getPlaceDetailsWithNearbyPlaces(placeId, languageCode, session);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "검색 화면 요청 / 개발 완료", description = "검색 화면 넘어갈 때 랜덤으로 이미지url이랑 placeId를 넘겨줘야함")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "이미지랑 id 성공적으로 반환",
                    content = @Content(schema = @Schema(implementation = PlaceImagePreviewResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "InternalServerException : 내부 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/search/image-preview")
    public ResponseEntity<List<PlaceImagePreviewResponseDto>> getPlaceImagePreviews(@RequestParam String languageCode) {
        List<PlaceImagePreviewResponseDto> response = placeService.getPlaceImagePreviews(languageCode);

        return ResponseEntity.ok(response);
    }



}
