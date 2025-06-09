package com.kroum.kroum.repository;

import com.kroum.kroum.dto.PlaceDto;
import com.kroum.kroum.dto.response.PlaceDetailsByPlaceIdResponseDto;
import com.kroum.kroum.dto.response.PlaceSearchResponseDto;
import com.kroum.kroum.entity.PlaceLanguage;
import com.kroum.kroum.repository.projection.NearbyPlaceProjection;
import com.kroum.kroum.repository.projection.PlaceDetailsProjection;
import com.kroum.kroum.repository.projection.PlaceImagePreviewProjection;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceLanguageRepository extends JpaRepository<PlaceLanguage, Long> {

    @Query("""
SELECT new com.kroum.kroum.dto.response.PlaceSearchResponseDto(
    p.latitude, p.longitude, p.firstImageUrl,
    pl.placeName, pl.description, pl.address,
    false,
    p.placeId
)
FROM PlaceLanguage pl
JOIN pl.place p
WHERE pl.place.placeId = :placeId
""")
    Optional<PlaceSearchResponseDto> findDtoByPlaceId(Long placeId);

    @Query("""
SELECT new com.kroum.kroum.dto.response.PlaceSearchResponseDto(
    p.latitude, p.longitude, p.firstImageUrl,
    pl.placeName, pl.description, pl.address,
    false,        
    p.placeId     
)
FROM PlaceLanguage pl
JOIN pl.place p
WHERE pl.place.placeId IN :placeIds
""")
    List<PlaceSearchResponseDto> findAllDtoByPlaceIdIn(List<Long> placeIds);

    /**
     * 거리 계산해주는 Native Query
     * @param latitude
     * @param longitude
     * @param languageCode
     * @param placeId
     * @return
     */
    @Query(value = """
SELECT 
    p.latitude AS latitude,
    p.longitude AS longitude,
    p.first_image_url AS firstImageUrl,
    pl.place_name AS placeName,
    pl.description AS description,
    pl.address AS address,
    p.place_id AS placeId,
    ST_Distance_Sphere(POINT(:lon, :lat), POINT(p.longitude, p.latitude)) AS distance
FROM place p
JOIN place_language pl ON pl.place_id = p.place_id
WHERE pl.language_code = :lang AND
      p.place_id != :placeId
HAVING distance <= 20000
ORDER BY distance ASC
""", nativeQuery = true)
    List<NearbyPlaceProjection> findNearbyPlacesWithinDistance(
            @Param("lat") double latitude,
            @Param("lon") double longitude,
            @Param("lang") String languageCode,
            @Param("placeId") Long placeId
    );

    Optional<PlaceLanguage> findByPlace_PlaceIdAndLanguage_LanguageCode(Long placeId, String languageCode);

    @Query(value = """
            SELECT pl.place_id AS placeId, p.first_image_url AS imageUrl
            FROM place_language pl
            JOIN place p ON pl.place_id = p.place_id
            WHERE pl.language_code = :languageCode
              AND p.first_image_url IS NOT NULL
              AND p.first_image_url <> ''
            ORDER BY RAND()
            LIMIT 3
            """, nativeQuery = true)
    List<PlaceImagePreviewProjection> findRandomPreviewsByLanguage(@Param("languageCode") String languageCode);

    /*@Query(value = """
SELECT 
    p.first_image_url AS firstImageUrl,
    pl.place_name AS placeName,
    pl.description AS description,
    pl.address AS address,
    p.place_id AS placeId
FROM place p
JOIN place_language pl ON pl.place_id = p.place_id
WHERE p.place_id = :placeId
""", nativeQuery = true)
    PlaceDetailsByPlaceIdResponseDto findPlaceDetailsByPlaceId(Long placeId);*/
    @Query(value = """
SELECT 
    p.first_image_url AS firstImageUrl,
    pl.place_name AS placeName,
    pl.description AS description,
    pl.address AS address,
    p.place_id AS placeId
FROM place p
JOIN place_language pl ON pl.place_id = p.place_id
WHERE p.place_id = :placeId
""", nativeQuery = true)
    PlaceDetailsProjection findPlaceDetailsByPlaceId(@Param("placeId") Long placeId);

    @Query("""
SELECT new com.kroum.kroum.dto.PlaceDto(
    null,
    p.latitude,
    p.longitude,
    p.firstImageUrl,
    pl.placeName,
    pl.description,
    pl.address,
    false,
    p.placeId
)
FROM Place p
LEFT JOIN PlaceLanguage pl ON pl.place = p AND pl.language.languageCode = :languageCode
WHERE p.placeId = :placeId
""")
    PlaceDto findPlaceByPlaceIdWithLanguage(@Param("placeId") Long placeId,
                                            @Param("languageCode") String languageCode);








}

