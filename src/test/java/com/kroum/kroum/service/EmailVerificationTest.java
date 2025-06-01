package com.kroum.kroum.service;

import com.kroum.kroum.dto.request.EmailVerificationCodeRequestDto;
import com.kroum.kroum.dto.request.EmailVerificationRequestDto;
import com.kroum.kroum.entity.EmailVerification;
import com.kroum.kroum.exception.EmailSendFailedException;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.repository.EmailVerificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationTest {

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void 올바른_이메일_요청이_주어지면_메일로_인증코드를_성공적으로_보낸다() {

        // Given
        EmailVerificationRequestDto requestDto = new EmailVerificationRequestDto("test@example.com");

        // When
        emailVerificationService.sendVerificationEmail(requestDto);

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void 이메일_전송_요청시_DB에_인증정보가_저장된다() {
        EmailVerificationRequestDto request = new EmailVerificationRequestDto("test@example.com");
        emailVerificationService.sendVerificationEmail(request);

        verify(emailVerificationRepository).save(any(EmailVerification.class));
    }

    @Test
    void 메일_전송에_실패하면_EmailSendFailedException을_던진다() {
        EmailVerificationRequestDto request = new EmailVerificationRequestDto("test@example.com");
        doThrow(new MailException("fail") {}).when(mailSender).send(any(SimpleMailMessage.class));

        assertThatThrownBy(() -> emailVerificationService.sendVerificationEmail(request))
                .isInstanceOf(EmailSendFailedException.class)
                .hasMessageContaining("이메일 전송에 실패");
    }

    @Test
    void 인증코드가_일치하면_true를_반환한다() {
        // Given
        String email = "test@example.com";
        String code = "123456";

        EmailVerification entity = EmailVerification.builder()
                .email(email)
                .verificationCode(code)
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email))
                .thenReturn(Optional.of(entity));

        EmailVerificationCodeRequestDto request = new EmailVerificationCodeRequestDto(email, code);

        // When
        boolean result = emailVerificationService.isVerificationCodeValid(request);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void 인증코드가_일치하지_않으면_false를_반환한다() {
        // Given
        String email = "test@example.com";
        String actualCode = "123456";
        String wrongCode = "654321";

        EmailVerification entity = EmailVerification.builder()
                .email(email)
                .verificationCode(actualCode)
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email))
                .thenReturn(Optional.of(entity));

        EmailVerificationCodeRequestDto request = new EmailVerificationCodeRequestDto(email, wrongCode);

        // When
        boolean result = emailVerificationService.isVerificationCodeValid(request);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void 인증정보가_없으면_InvalidRequestException을_던진다() {
        // Given
        String email = "test@example.com";
        String code = "123456";

        when(emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email))
                .thenReturn(Optional.empty());

        EmailVerificationCodeRequestDto request = new EmailVerificationCodeRequestDto(email, code);

        // When + Then
        assertThatThrownBy(() -> emailVerificationService.isVerificationCodeValid(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("존재하지 않습니다");
    }




}
