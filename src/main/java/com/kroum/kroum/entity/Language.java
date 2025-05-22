package com.kroum.kroum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @Column(name = "language_code", length = 2)
    private String languageCode;

    @Column(name = "language_name", length = 30)
    private String languageName;
}