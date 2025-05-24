package com.kroum.kroum.repository;

import com.kroum.kroum.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUser_IdAndPlace_PlaceId(Long userId, Long placeId);

    int countByPlace_PlaceId(Long placeId);


}