package com.kroum.kroum.repository;

import com.kroum.kroum.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SearchHistory> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SearchHistory> findByUserIdAndSearchText(Long userId, String searchText);

    void deleteAllByUserId(Long userId);


}