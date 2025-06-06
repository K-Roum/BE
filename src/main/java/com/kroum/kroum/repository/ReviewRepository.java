package com.kroum.kroum.repository;

import com.kroum.kroum.dto.response.PlaceReviewDto;
import com.kroum.kroum.dto.response.ReviewDetailResponseDto;
import com.kroum.kroum.dto.response.ReviewSummaryResponseDto;
import com.kroum.kroum.entity.Language;
import com.kroum.kroum.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByPlace_PlaceId(Long placeId);

    @Query("""
    SELECT new com.kroum.kroum.dto.response.PlaceReviewDto(
        u.nickname,
        r.content,
        r.rating,
        r.createdAt
    )
    FROM Review r
    JOIN r.user u
    WHERE r.place.placeId = :placeId
""")
    List<PlaceReviewDto> findPlaceReviewDtosByPlaceId(Long placeId);

    @Query("""
    SELECT AVG(r.rating)
    FROM Review r
    WHERE r.place.placeId = :placeId
""")
    Double findAverageRatingByPlaceId(Long placeId);

    @Query("""
        SELECT new com.kroum.kroum.dto.response.ReviewSummaryResponseDto(
            p.placeId,
            p.firstImageUrl,
            r.rating,
            pl.placeName
        )
        FROM Review r
        JOIN r.place p
        JOIN PlaceLanguage pl ON pl.place = p AND pl.language = :language
        WHERE r.user.id = :userId
        GROUP BY p.placeId, p.firstImageUrl, pl.placeName
    """)
    List<ReviewSummaryResponseDto> findReviewSummariesByUserId(Long userId);

    @Query("""
        SELECT new com.kroum.kroum.dto.response.ReviewDetailResponseDto(
            pl.placeName,
            r.rating,
            r.content,
            FUNCTION('DATE_FORMAT', r.createdAt, '%Y-%m-%d'),
            p.firstImageUrl
        )
        FROM Review r
        JOIN r.place p
        JOIN PlaceLanguage pl ON pl.place = p AND pl.language = :language
        WHERE r.user.id = :userId
    """)
    List<ReviewDetailResponseDto> findReviewDetailsByUserId(Long userId);

}


