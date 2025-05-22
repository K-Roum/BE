package com.kroum.kroum.repository;

import com.kroum.kroum.dto.response.PlaceSearchResponseDto;
import com.kroum.kroum.entity.PlaceLanguage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceLanguageRepository extends JpaRepository<PlaceLanguage, Long> {

    @Query("""
SELECT new com.kroum.kroum.dto.response.PlaceSearchResponseDto(
    p.latitude, p.longitude, p.firstImageUrl,
    pl.placeName, pl.description, pl.address
)
FROM PlaceLanguage pl
JOIN pl.place p
WHERE pl.place.placeId = :placeId
""")
    Optional<PlaceSearchResponseDto> findDtoByPlaceIdAndLanguage(Long placeId);

    @Query("""
SELECT new com.kroum.kroum.dto.response.PlaceSearchResponseDto(
    p.latitude, p.longitude, p.firstImageUrl,
    pl.placeName, pl.description, pl.address
)
FROM PlaceLanguage pl
JOIN pl.place p
WHERE pl.place.placeId IN :placeIds
""")
    List<PlaceSearchResponseDto> findAllDtoByPlaceIdIn(List<Long> placeIds);

}

