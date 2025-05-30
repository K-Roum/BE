package com.kroum.kroum.service;

import com.kroum.kroum.dto.request.PlaceSearchRequestDto;
import com.kroum.kroum.dto.response.*;
import com.kroum.kroum.entity.Place;
import com.kroum.kroum.exception.InternalServerException;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.repository.BookmarkRepository;
import com.kroum.kroum.repository.PlaceLanguageRepository;
import com.kroum.kroum.repository.PlaceRepository;
import com.kroum.kroum.repository.ReviewRepository;
import com.kroum.kroum.repository.projection.NearbyPlaceProjection;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PlaceRepository placeRepository;
    private final PlaceLanguageRepository placeLanguageRepository;
    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;

    // 프론트로부터 받은 검색 요청 DTO를 추가 정보를 덧붙여서 AI 서버에게 ID 리턴해달라고 요청하는 메서드
    public List<ContentIdDto> getRecommendedPlaceIds(PlaceSearchRequestDto request) {
        //String url = "http://127.0.0.1:5000/ai/search";
        String url = "http://202.31.202.172:80/jem/ai/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PlaceSearchRequestDto> entity = new HttpEntity<>(request, headers);

        // ResponseEntity 타입으로 파이썬에 요청 날리기
        // try - catch에 잡힌다면 AI 서버와 통신 자체가 안 되는 것, 안 잡힌다면 통신은 되는데 AI 서버에서 에러코드 던져주는 것
        try {
            ResponseEntity<List<ContentIdDto>> response = restTemplate.exchange(
                    url.toString(),
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<ContentIdDto>>() {}
            );

            // 상태코드 파싱
            HttpStatusCode statusCode = response.getStatusCode();

            // 400 에러, 500 에러 처리 << 파이썬에서 400 500으로 던져줌
            if (statusCode.is4xxClientError()) throw new InvalidRequestException("AI 서버로부터 잘못된 요청 응답을 받았습니다.");

            if (statusCode.is5xxServerError()) throw new InternalServerException("AI 서버 내부 오류");

            // 에러 안 터지면 바디 파싱해서 id들 받아주기
            List<ContentIdDto> ids = response.getBody();

            /* 파싱한 ids도 비어있으면 리스트 초기화해서 던져줌
               안 비어있다면 그대로 리턴해줌 (통신도 정상적이고, 리스트에 실제 ContentId(=placeId)들도 들어있음
             */
            if (ids == null || ids.isEmpty()) return Collections.emptyList();

            return ids;
        }

        catch (RestClientException e) {
            throw new InternalServerException("AI 서버와 통신 자체가 불가능한 상황입니다.");
        }
    }

    /*

     */
    // 이거 넘겨줄 때 찜 여부도 넘겨줘야 할 것 같은데 그럼 Dto 수정 할 필요가 있어 보임
    // 아아아아악!!!!!!!!!!!
    public List<PlaceSearchResponseDto> getPlacesByIds(List<ContentIdDto> ids) {

        List<Long> placeIds = ids.stream()
                .map(ContentIdDto::getContentId)
                .toList();

        return placeLanguageRepository.findAllDtoByPlaceIdIn(placeIds);
    }

    /**
     * 해당 장소에 대한 리뷰 목록을 들고 온다.
     * 들고 오는 정보는 장소에 대한 리뷰 갯수, 리뷰 평균 평점, 리뷰 리스트(닉네임, 리뷰 내용, 리뷰 별점, 작성 일시)
     * @param placeId
     * @return
     */
    public PlaceReviewsResponseDto getReviewsByPlaceId(Long placeId) {
        Double avg = getAverageRating(placeId);
        Long totalCount = reviewRepository.countByPlace_PlaceId(placeId);
        List<PlaceReviewDto> placeReviews = reviewRepository.findDtoByPlaceId(placeId);
        PlaceReviewsResponseDto responseDto = new PlaceReviewsResponseDto(totalCount, avg, placeReviews);

        return responseDto;

    }

    public double getAverageRating(Long placeId) {
        Double avg = reviewRepository.findAverageRatingByPlaceId(placeId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0; // 소수점 1자리 반올림
    }

    /**
     * 로그인된 사용자인 경우, 해당 사용자가 특정 장소를 찜했는지 여부를 반환한다.
     *
     * @param session 로그인된 사용자의 세션. 세션에 userId가 없으면 로그인되지 않은 상태로 간주함
     * @param placeId 찜 여부를 확인할 장소의 ID
     * @return 사용자가 해당 장소를 찜했다면 true, 아니면 false
     */
    public boolean isBookmarked(HttpSession session, Long placeId) {

        if (session == null) return false;

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return false;

        return bookmarkRepository.existsByUser_IdAndPlace_PlaceId(userId, placeId);
    }

    /**
     *
     * @param session
     * @param placeId
     * @return
     */
    /*public PlaceDetailsResponseDto getPlaceDetails(HttpSession session, Long placeId) {

    }*/

    /**
     * 내가 선택한 장소로부터 인근에 있는 장소 리스트를 반환한다.
     * 최대 반경은 20km, 거리를 계산해서 프론트단에서 1 3 5 10 20 등으로 나누어 지도 확대 축소 기능을 넣어서 출력해주도록 한다.
     */
    //public List<>


    public List<NearbyPlaceResponseDto> getNearbyPlaces(Long placeId, String langCode, HttpSession session) {
        // 기준 장소의 위경도 가져오기
        Place originPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new InvalidRequestException("기준 장소를 찾을 수 없습니다."));

        double originPlaceLatitude = originPlace.getLatitude();
        double originPlaceLongitude = originPlace.getLongitude();

        // 주변 장소 쿼리 실행
        List<NearbyPlaceProjection> raw = placeLanguageRepository.findNearbyPlacesWithinDistance(
                originPlaceLatitude, originPlaceLongitude, langCode, placeId
        );

        // 사용자 로그인 여부 확인
        Long userId = session != null ? (Long) session.getAttribute("userId") : null;

        // 결과 매핑
        return raw.stream()
                .map(p -> {
                    boolean isBookmarked = false;
                    if (userId != null)
                        isBookmarked = bookmarkRepository.existsByUser_IdAndPlace_PlaceId(userId, p.getPlaceId());

                    return new NearbyPlaceResponseDto(
                            new PlaceSearchResponseDto(
                                    p.getLatitude(), p.getLongitude(), p.getFirstImageUrl(),
                                    p.getPlaceName(), p.getDescription(), p.getAddress()
                            ),
                            p.getDistance()
                    );
                })
                .toList();
    }

    public PlaceDetailsWithNearbyPlacesResponseDto getPlaceDetailsWithNearbyPlaces(Long placeId, String langCode, HttpSession session) {
        // 1. 리뷰 정보 수집
        PlaceReviewsResponseDto reviews = getReviewsByPlaceId(placeId);

        // 2. 북마크 정보 수집
        int bookmarkCount = bookmarkRepository.countByPlace_PlaceId(placeId);
        boolean isBookmarked = isBookmarked(session, placeId);
        PlaceBookmarkDto bookmark = new PlaceBookmarkDto(bookmarkCount, isBookmarked);

        // 3. 상세 정보 조립
        PlaceDetailsResponseDto details = new PlaceDetailsResponseDto(reviews, bookmark);

        // 4. 주변 장소 리스트 생성
        List<NearbyPlaceResponseDto> nearby = getNearbyPlaces(placeId, langCode, session);

        // 5. 최종 응답 조립
        return new PlaceDetailsWithNearbyPlacesResponseDto(details, nearby);
    }





}
