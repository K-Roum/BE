package com.kroum.kroum.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_verification_id")
    private Long id;

    /*@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;*/

    @Column(name = "email", nullable = false)
    private String email; // User 없이 이메일만 저장

    @Column(name = "verification_code", length = 30, nullable = false)
    private String verificationCode;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = true)
    private LocalDateTime expiresAt;
}
