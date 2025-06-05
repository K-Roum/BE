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

        searchHistoryRepository.save(new SearchHistory(userId, request.getQuery()));

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
