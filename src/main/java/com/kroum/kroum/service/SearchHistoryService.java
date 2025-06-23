package com.kroum.kroum.service;

import com.kroum.kroum.dto.request.SaveSearchHistoryRequestDto;
import com.kroum.kroum.dto.response.SearchHistoryResponseDto;
import com.kroum.kroum.entity.SearchHistory;
import com.kroum.kroum.repository.SearchHistoryRepository;
import com.kroum.kroum.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public void save(Long userId, SaveSearchHistoryRequestDto request) {
        String query = request.getQuery();

        // 1. 가장 최근 검색어 조회
        SearchHistory latestHistory = searchHistoryRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElse(null);

        // 2. 연속 중복이면 저장 안 함
        if (latestHistory != null && latestHistory.getSearchText().equals(query)) {
            return;
        }

        // 3. 기존에 존재하는 검색어가 있으면 createdAt만 갱신
        SearchHistory existingHistory = searchHistoryRepository.findByUserIdAndSearchText(userId, query)
                .orElse(null);

        if (existingHistory != null) {
            existingHistory.updateCreatedAt();
            searchHistoryRepository.save(existingHistory);
        } else {
            searchHistoryRepository.save(new SearchHistory(userId, query));
        }
    }


    public List<SearchHistoryResponseDto> getSearchHistories(HttpSession session) {

        Long userId = SessionUtil.getLoginUserId(session);
        List<SearchHistory> histories = searchHistoryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return histories.stream()
                .map(h -> new SearchHistoryResponseDto(
                        h.getSearchText(),
                        h.getCreatedAt().format(formatter)
                ))
                .toList();

    }



}
