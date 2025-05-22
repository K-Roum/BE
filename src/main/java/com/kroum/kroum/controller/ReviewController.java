package com.kroum.kroum.controller;

import com.kroum.kroum.dto.request.ReviewCreateRequestDto;
import com.kroum.kroum.dto.request.ReviewUpdateRequestDto;
import com.kroum.kroum.dto.response.ApiResponseDto;
import com.kroum.kroum.dto.response.ReviewDetailResponseDto;
import com.kroum.kroum.dto.response.ReviewSummaryResponseDto;
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

@Tag(name = "Review API", description = "리뷰 관련 컨트롤러")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

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
                                                       HttpSession session)
    {
        // 서비스 로직 구현해야함


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
        // 서비스 로직 구현해야함


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
        // 서비스 로직 구현해야함


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
    public ResponseEntity<List<ReviewDetailResponseDto>> getReviewsByPlaceId(@RequestParam Long placeId) {

        // 서비스 미구현: mock 데이터로 대체
        // 같은 장소에 대한 리뷰들을 가져오는 것임
        List<ReviewDetailResponseDto> response = List.of(
                new ReviewDetailResponseDto(
                        "경복궁",
                        4,
                        "역사적인 분위기에서 산책하기 너무 좋아요.",
                        "2025-05-12",
                        "https://cdn.kroum.com/places/gyungbok.jpg"
                ),
                new ReviewDetailResponseDto(
                        "경복궁",
                        3,
                        "야경이 예뻤어요. 낮보다 밤 추천!",
                        "2025-05-11",
                        "https://cdn.kroum.com/places/gyungbok.jpg"
                )
        );

        return ResponseEntity.ok(response);
    }

    //**내 리뷰 요약 조회**
    //
    //**GET /summary**

    @Operation(summary = "마이페이지에서 내가 작성한 리뷰 요약 조회", description = "마이페이지 버튼을 누르면 이 컨트롤러 호출, 조합해서 마이페이지 찍어준다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 리뷰 목록 호출 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewSummaryResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "마이페이지 접근 불가 - 로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/summary")
    public ResponseEntity<List<ReviewSummaryResponseDto>> getSummaryReviews(HttpSession session) {
        // 서비스 미구현: mock 데이터로 대체
        List<ReviewSummaryResponseDto> myReviews = List.of(
                new ReviewSummaryResponseDto(
                        123L,
                        "https://cdn.kroum.com/places/gyungbok.jpg",
                        4.5,
                        "경복궁"
                ),
                new ReviewSummaryResponseDto(
                        456L,
                        "https://cdn.kroum.com/places/gyungbok123.jpg",
                        2.3,
                        "창덕궁"
                )
        );

        return ResponseEntity.ok(myReviews);
    }

    @Operation(summary = "마이페이지에서 내가 작성한 리뷰 상세 조회", description = "리뷰 목록 버튼을 누르면 호출.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 상세 리뷰 목록 호출 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDetailResponseDto.class)))),
            // @ApiResponse(responseCode = "401", description = "마이페이지 접근 불가 - 로그인이 필요함"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/detail")
    public ResponseEntity<List<ReviewDetailResponseDto>> getDetailReviews(HttpSession session) {
        // 서비스 미구현: mock 데이터로 대체
        List<ReviewDetailResponseDto> myDetailReviews = List.of(
                new ReviewDetailResponseDto(
                        "경복궁",
                        5,
                        "역사적인 분위기에서 산책하기 너무 좋아요.",
                        "2025-05-12",
                        "https://cdn.kroum.com/places/gyungbok.jpg"
                ),
                new ReviewDetailResponseDto(
                        "우리집",
                        4,
                        "진짜 편해요!",
                        "2025-05-11",
                        "https://cdn.kroum.com/places/gyungbok.jpg"
                )
        );

        return ResponseEntity.ok(myDetailReviews);
    }
}
