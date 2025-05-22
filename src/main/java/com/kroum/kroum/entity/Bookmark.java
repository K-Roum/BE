package com.kroum.kroum.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "place_language_id", nullable = false)
    private PlaceLanguage placeLanguage;

    private LocalDateTime createdAt;
}
