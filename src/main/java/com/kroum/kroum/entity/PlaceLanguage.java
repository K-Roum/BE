package com.kroum.kroum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place_language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_language_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "language_code", nullable = false)
    private Language language;

    @Column(name = "place_name", length = 255)
    private String placeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String address;
}