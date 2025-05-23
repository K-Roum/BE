package com.kroum.kroum.repository;

import com.kroum.kroum.dto.response.PlaceReviewDto;
import com.kroum.kroum.dto.response.PlaceSearchResponseDto;
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
    List<PlaceReviewDto> findDtoByPlaceId(Long placeId);

    @Query("""
    SELECT AVG(r.rating)
    FROM Review r
    WHERE r.place.placeId = :placeId
""")
    Double findAverageRatingByPlaceId(Long placeId);

}
