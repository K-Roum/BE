package com.kroum.kroum.service;

import com.kroum.kroum.dto.request.PlaceSearchRequestDto;
import com.kroum.kroum.dto.response.*;
import com.kroum.kroum.entity.Place;
import com.kroum.kroum.exception.InternalServerException;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.repository.*;
import com.kroum.kroum.repository.projection.NearbyPlaceProjection;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock private PlaceRepository placeRepository;
    @Mock private PlaceLanguageRepository placeLanguageRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private BookmarkRepository bookmarkRepository;
    @Mock private HttpSession session;

    @InjectMocks
    private PlaceService placeService;

    @Test
    void getAverageRating() {
        when(reviewRepository.findAverageRatingByPlaceId(1L)).thenReturn(3.66);
        double result = placeService.getAverageRating(1L);
        assertThat(result).isEqualTo(3.7);
    }

    @Test
    void getAverageRating_null() {
        when(reviewRepository.findAverageRatingByPlaceId(1L)).thenReturn(null);
        double result = placeService.getAverageRating(1L);
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    void isBookmarked_true() {
        when(session.getAttribute("userId")).thenReturn(1L);
        when(bookmarkRepository.existsByUser_IdAndPlace_PlaceId(1L, 100L)).thenReturn(true);
        assertThat(placeService.isBookmarked(session, 100L)).isTrue();
    }

    @Test
    void isBookmarked_false() {
        when(session.getAttribute("userId")).thenReturn(null);
        assertThat(placeService.isBookmarked(session, 100L)).isFalse();
    }

    @Test
    void getReviewsByPlaceId() {
        when(reviewRepository.countByPlace_PlaceId(1L)).thenReturn(3L);
        when(reviewRepository.findAverageRatingByPlaceId(1L)).thenReturn(4.5);
        List<PlaceReviewDto> reviews = List.of(new PlaceReviewDto("user", "good", 5, LocalDateTime.now()));
        when(reviewRepository.findDtoByPlaceId(1L)).thenReturn(reviews);

        PlaceReviewsResponseDto result = placeService.getReviewsByPlaceId(1L);
        assertThat(result.getTotalCount()).isEqualTo(3);
        assertThat(result.getAverageRating()).isEqualTo(4.5);
        assertThat(result.getPlacesReviews()).hasSize(1);
    }

    @Test
    void getPlacesByIds() {
        List<ContentIdDto> ids = List.of(new ContentIdDto(1L));
        when(bookmarkRepository.findPlaceIdsByUserId(anyLong())).thenReturn(List.of(1L));
        when(session.getAttribute("userId")).thenReturn(1L);

        PlaceSearchResponseDto dto = new PlaceSearchResponseDto();
        dto.setPlaceId(1L);
        when(placeLanguageRepository.findAllDtoByPlaceIdIn(List.of(1L))).thenReturn(List.of(dto));

        List<PlaceSearchResponseDto> result = placeService.getPlacesByIds(ids, session);
        assertThat(result.get(0).isBookmarked()).isTrue();
    }

    @Test
    void getNearbyPlaces() {
        Place origin = new Place();
        origin.setLatitude(10.0);
        origin.setLongitude(20.0);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(origin));

        NearbyPlaceProjection projection = mock(NearbyPlaceProjection.class);
        when(projection.getLatitude()).thenReturn(10.0);
        when(projection.getLongitude()).thenReturn(20.0);
        when(projection.getPlaceId()).thenReturn(2L);
        when(projection.getDistance()).thenReturn(1.0);
        when(projection.getPlaceName()).thenReturn("Test");
        when(projection.getDescription()).thenReturn("Desc");
        when(projection.getAddress()).thenReturn("Addr");
        when(projection.getFirstImageUrl()).thenReturn("img.jpg");

        when(placeLanguageRepository.findNearbyPlacesWithinDistance(anyDouble(), anyDouble(), any(), anyLong()))
                .thenReturn(List.of(projection));
        when(session.getAttribute("userId")).thenReturn(1L);
        when(bookmarkRepository.findPlaceIdsByUserId(1L)).thenReturn(List.of(2L));

        List<NearbyPlaceResponseDto> result = placeService.getNearbyPlaces(1L, "ko", session);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPlace().isBookmarked()).isTrue();
    }

   /* @Test
    void getRecommendedPlaceIds_success() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        List<ContentIdDto> ids = List.of(new ContentIdDto(1L));
        ResponseEntity<List<ContentIdDto>> response = new ResponseEntity<>(ids, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);
        // 추후 restTemplate 주입형으로 변경 시 적용
    }*/

    /*@Test
    void getRecommendedPlaceIds_400() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ResponseEntity<List<ContentIdDto>> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);
        // 예외 테스트 필요 (InvalidRequestException)
    }

    @Test
    void getPlaceDetailsWithNearbyPlaces() {
        // 통합 호출이므로 각 서브 메서드 mock 처리 필요
        // 생략 가능, 또는 SpringBootTest로 별도 작성 권장
    }*/
}
