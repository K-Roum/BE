package com.kroum.kroum.service;


import com.kroum.kroum.dto.request.EmailVerificationCodeRequestDto;
import com.kroum.kroum.dto.request.EmailVerificationRequestDto;
import com.kroum.kroum.entity.EmailVerification;
import com.kroum.kroum.exception.EmailSendFailedException;
import com.kroum.kroum.exception.InvalidRequestException;
import com.kroum.kroum.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationEmail(EmailVerificationRequestDto request) {

        // 이런 예외처리는 DTO가 책임져야함!
        /*if (request.getEmail() == null)
            throw new InvalidRequestException("이메일을 기입해주세요.");*/

        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = createMessage(
                request.getEmail(),
                "[Kroum] 회원가입 인증 코드입니다.",
                "인증 코드: " + verificationCode
        );

        try
        {
            javaMailSender.send(message);
        }

        catch (MailException e)
        {
            throw new EmailSendFailedException("이메일 전송에 실패했습니다. 다시 시도해주세요.");
        }


        EmailVerification emailVerification = EmailVerification.builder()
                .email(request.getEmail())
                .verificationCode(verificationCode)
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(null)
                .build();

        emailVerificationRepository.save(emailVerification);

    }

    public boolean isVerificationCodeValid(EmailVerificationCodeRequestDto request) {

        Optional<EmailVerification> verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail());

        if (verification.isEmpty())
            throw new InvalidRequestException("해당 이메일로 발송된 인증 코드가 존재하지 않습니다.");

        return verification
                .map(v -> v.getVerificationCode().equals(request.getCode()))
                .orElse(false);

    }


    private String generateVerificationCode() {
        // 인증 코드 생성만
        return String.valueOf((int)((Math.random() * 900000) + 100000)); // 6자리 숫자
    }

    private SimpleMailMessage createMessage(String to, String subject, String text) {
        // 메시지 바디 만드는 용도
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("4pril17@naver.com");

        return message;
    }


}
