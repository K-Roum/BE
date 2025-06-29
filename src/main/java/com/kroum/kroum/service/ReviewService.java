package com.kroum.kroum.service;

import com.kroum.kroum.dto.request.ReviewCreateRequestDto;
import com.kroum.kroum.dto.request.ReviewUpdateRequestDto;
import com.kroum.kroum.dto.response.PlaceReviewDto;
import com.kroum.kroum.dto.response.PlaceReviewsResponseDto;
import com.kroum.kroum.dto.response.ReviewDetailResponseDto;
import com.kroum.kroum.dto.response.ReviewSummaryResponseDto;
import com.kroum.kroum.entity.Place;
import com.kroum.kroum.entity.Review;
import com.kroum.kroum.entity.User;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.repository.PlaceRepository;
import com.kroum.kroum.repository.ReviewRepository;
import com.kroum.kroum.repository.UserRepository;
import com.kroum.kroum.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public void createReview(ReviewCreateRequestDto request, Long placeId, HttpSession session) {

        Long userId = SessionUtil.getLoginUserId(session);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("해당 장소를 찾을 수 없습니다."));

        Optional<Review> check = reviewRepository.findByPlaceIdAndUserId(placeId, userId);

        if (check.isPresent())
            throw new InvalidRequestException("이미 해당 장소에 대한 리뷰가 존재합니다. 리뷰를 수정해주세요.");

        Review review = Review.builder()
                .user(user)
                .place(place)
                .content(request.getContent())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

    }

    public void updateReview(Long placeId, ReviewUpdateRequestDto request, HttpSession session) {
        Long userId = SessionUtil.getLoginUserId(session);
        if (userId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        Review review = reviewRepository.findByPlaceIdAndUserId(placeId, userId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 리뷰만 수정할 수 있습니다.");
        }

        review.setContent(request.getContent());
        review.setRating(request.getRating());

        reviewRepository.save(review);
    }



    public void deleteReview(Long placeId, HttpSession session) {

        Long userId = SessionUtil.getLoginUserId(session);
        if (userId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Review review = reviewRepository.findByPlaceIdAndUserId(placeId, userId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // 작성자 본인인지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

    public PlaceReviewsResponseDto getPlaceReviewList(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("해당 장소를 찾을 수 없습니다."));

        Double avg = reviewRepository.findAverageRatingByPlaceId(placeId);
        double roundedAvg = (avg != null) ? Math.round(avg * 10.0) / 10.0 : 0.0;

        Long count = reviewRepository.countByPlace_PlaceId(placeId);
        List<PlaceReviewDto> reviews = reviewRepository.findPlaceReviewDtosByPlaceId(placeId);

        return new PlaceReviewsResponseDto(count, roundedAvg, reviews);
    }


    public List<ReviewSummaryResponseDto> getMyReviewSummaries(HttpSession session) {
        Long userId = SessionUtil.requireLoginUserId(session);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        return reviewRepository.findReviewSummariesByUserId(userId);
    }

    public List<ReviewDetailResponseDto> getMyFullReviews(HttpSession session) {
        Long userId = SessionUtil.requireLoginUserId(session);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        return reviewRepository.findReviewDetailsByUserId(userId);
    }
}
