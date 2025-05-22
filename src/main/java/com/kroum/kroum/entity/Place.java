package com.kroum.kroum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "place")
public class Place {

    @Id
    @Column(name = "place_id")
    private Long placeId; // contentId로 제공된 외부 데이터 사용 → autoincrement 아님

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true) // nullable = true (사용 안 할 수도 있어서)
    private Category category;

    private double latitude;
    private double longitude;

    @Column(name = "first_image_url", columnDefinition = "TEXT")
    private String firstImageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

