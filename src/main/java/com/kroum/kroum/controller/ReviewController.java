package com.kroum.kroum.controller;

import com.kroum.kroum.dto.request.ReviewCreateRequestDto;
import com.kroum.kroum.dto.request.ReviewUpdateRequestDto;
import com.kroum.kroum.dto.response.ApiResponseDto;
import com.kroum.kroum.dto.response.PlaceReviewsResponseDto;
import com.kroum.kroum.dto.response.ReviewDetailResponseDto;
import com.kroum.kroum.dto.response.ReviewSummaryResponseDto;
import com.kroum.kroum.entity.Language;
import com.kroum.kroum.service.ReviewService;
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

@Tag(name = "Review API", description = "리뷰 관련 컨트롤러")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "별점, 리뷰 내용 작성해서 컨트롤러 호출")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 리뷰가 등록됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{placeId}")
    public ResponseEntity<ApiResponseDto> createReview(@PathVariable Long placeId,
                                                       @RequestBody ReviewCreateRequestDto request,
                                                       HttpSession session) {

        reviewService.createReview(request, placeId, session);

        return ResponseEntity.ok(new ApiResponseDto(true, "리뷰가 성공적으로 등록되었습니다."));
    }


    @Operation(summary = "리뷰 수정", description = "별점, 리뷰 내용 수정해서 컨트롤러 호출")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 리뷰가 수정됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDto> updateReview(@PathVariable Long reviewId,
                                                       @RequestBody ReviewUpdateRequestDto request,
                                                       HttpSession session)
    {
        reviewService.updateReview(reviewId, request, session);

        return ResponseEntity.ok(new ApiResponseDto(true, "리뷰가 성공적으로 수정되었습니다."));
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제 버튼을 누르면 컨트롤러 호출")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 리뷰가 삭제됨",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDto> deleteReview(@PathVariable Long reviewId,
                                                       HttpSession session)
    {
        reviewService.deleteReview(reviewId, session);

        return ResponseEntity.ok(new ApiResponseDto(true, "리뷰가 성공적으로 삭제되었습니다."));
    }

    @Operation(summary = "특정 장소 리뷰 조회", description = "상세보기 버튼을 누르면 컨트롤러 호출")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 관광지에 대한 리뷰 목록 호출",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDetailResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<PlaceReviewsResponseDto> getReviewsByPlaceId(@RequestParam Long placeId) {

        PlaceReviewsResponseDto response = reviewService.getPlaceReviewList(placeId);

        return ResponseEntity.ok(response);
    }

    //**내 리뷰 요약 조회**
    //
    //**GET /summary**

    @Operation(summary = "마이페이지에서 내가 작성한 리뷰 요약 조회 / 구현 완료", description = "마이페이지 버튼을 누르면 이 컨트롤러 호출, 조합해서 마이페이지 찍어준다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 리뷰 목록 호출 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewSummaryResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "마이페이지 접근 불가 - 로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/summary")
    public ResponseEntity<List<ReviewSummaryResponseDto>> getSummaryReviews(HttpSession session) {

        List<ReviewSummaryResponseDto> myReviewSummaries = reviewService.getMyReviewSummaries(session);

        return ResponseEntity.ok(myReviewSummaries);
    }

    @Operation(summary = "마이페이지에서 내가 작성한 리뷰 상세 조회 / 구현 완료", description = "리뷰 목록 버튼을 누르면 호출.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 상세 리뷰 목록 호출 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDetailResponseDto.class)))),
            // @ApiResponse(responseCode = "401", description = "마이페이지 접근 불가 - 로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/detail")
    public ResponseEntity<List<ReviewDetailResponseDto>> getDetailReviews(HttpSession session) {
        log.info("내가 작성한 리뷰 상세 조회 컨트롤러 호출");
        List<ReviewDetailResponseDto> myDetailReviews = reviewService.getMyFullReviews(session);

        return ResponseEntity.ok(myDetailReviews);
    }
}
