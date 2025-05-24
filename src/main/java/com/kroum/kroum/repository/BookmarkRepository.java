package com.kroum.kroum.repository;

import com.kroum.kroum.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUser_IdAndPlace_PlaceId(Long userId, Long placeId);

    int countByPlace_PlaceId(Long placeId);

    @Query("SELECT b.place.placeId FROM Bookmark b WHERE b.user.id = :userId")
    List<Long> findPlaceIdsByUserId(@Param("userId") Long userId);



}