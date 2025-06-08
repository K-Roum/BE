package com.kroum.kroum.service;

import com.kroum.kroum.dto.response.BookmarkResponseDto;
import com.kroum.kroum.entity.Bookmark;
import com.kroum.kroum.entity.Place;
import com.kroum.kroum.entity.PlaceLanguage;
import com.kroum.kroum.entity.User;
import com.kroum.kroum.repository.BookmarkRepository;
import com.kroum.kroum.repository.PlaceLanguageRepository;
import com.kroum.kroum.repository.PlaceRepository;
import com.kroum.kroum.repository.UserRepository;
import com.kroum.kroum.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;
    private final PlaceLanguageRepository placeLanguageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addBookmark(Long placeId, HttpSession session) {
        Long userId = SessionUtil.getLoginUserId(session);
        log.info("[찜 요청] 세션 ID: {}", session.getId());

        // 중복 체크
        if (bookmarkRepository.existsByUser_IdAndPlace_PlaceId(userId, placeId)) {
            deleteBookmark(placeId, session);
        }

        // 필요한 엔티티 로딩
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("장소를 찾을 수 없습니다."));
        PlaceLanguage placeLanguage = placeLanguageRepository.findByPlace_PlaceIdAndLanguage_LanguageCode(
                placeId, "ko"
        ).orElseThrow(() -> new NoSuchElementException("다국어 장소 정보를 찾을 수 없습니다."));

        // Bookmark 생성
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setPlace(place);
        bookmark.setPlaceLanguage(placeLanguage);
        bookmark.setCreatedAt(LocalDateTime.now());

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long placeId, HttpSession session) {
        Long userId = SessionUtil.getLoginUserId(session);

        // 북마크 조회
        Bookmark bookmark = bookmarkRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(userId) && b.getPlace().getPlaceId().equals(placeId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 찜을 찾을 수 없습니다."));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponseDto> getBookmarks(HttpSession session) {
        Long userId = SessionUtil.getLoginUserId(session);

        // 북마크 전체 조회
        List<Bookmark> bookmarks = bookmarkRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(userId))
                .toList();

        return bookmarks.stream()
                .map(b -> new BookmarkResponseDto(
                        b.getPlace().getPlaceId(),
                        b.getPlaceLanguage().getPlaceName(),
                        b.getCreatedAt().toLocalDate().toString(),
                        b.getPlace().getFirstImageUrl()
                ))
                .toList();
    }

}
