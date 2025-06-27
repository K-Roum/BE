package com.kroum.kroum.service;

import com.kroum.kroum.dto.request.PasswordChangeRequestDto;
import com.kroum.kroum.dto.request.ProfileUpdateRequestDto;
import com.kroum.kroum.repository.projection.PlaceDetailsProjection;

import com.kroum.kroum.dto.response.*;
import com.kroum.kroum.entity.Bookmark;
import com.kroum.kroum.entity.EmailVerification;
import com.kroum.kroum.repository.*;
import com.kroum.kroum.util.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.kroum.kroum.dto.request.LoginRequestDto;
import com.kroum.kroum.dto.request.SignupRequestDto;
import com.kroum.kroum.entity.User;
import com.kroum.kroum.exception.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ReviewRepository reviewRepository;
    private final PlaceLanguageRepository placeLanguageRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final SearchHistoryRepository searchHistoryRepository;

    public void signUp(SignupRequestDto request) {

        if (userRepository.existsByLoginId(request.getLoginId()))
            throw new DuplicateException("이미 존재하는 아이디입니다.");


        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateException("이미 존재하는 이메일입니다.");


        if (userRepository.existsByNickname(request.getNickname()))
            throw new DuplicateException("이미 존재하는 닉네임입니다.");


        User user = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .email(request.getEmail())
                .build();

        userRepository.save(user);
    }

    public boolean isDuplicateLoginId(String loginId) {

        return userRepository.existsByLoginId(loginId);
    }

    public boolean isDuplicateEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    public boolean isDuplicateNickname(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    public HttpSession login(LoginRequestDto request, HttpSession session) {

        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UnauthenticatedException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthenticatedException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("userId", user.getId());
        log.info("로그인 완료 세션 ID: {}", session.getId());

        return session;
    }

    /*public void logout(HttpSession session) {

        log.info("[로그아웃 요청] 세션 ID: {}", session.getId());

        Long userId = SessionUtil.getLoginUserId(session);

        if (session.getAttribute("userId") == null)
            throw new InvalidRequestException("유효하지 않은 요청입니다.");

        session.invalidate();

        log.info("[로그아웃 성공] userId: {}", userId);
    }*/


    // 추후 디버깅용 로그 제거하기
    public void logout(HttpSession session) {
        if (session == null) {
            log.info("[로그아웃 요청] 세션 없음 - 이미 로그아웃 상태일 수 있음");
            return;
        }

        log.info("[로그아웃 요청] 세션 ID: {}", session.getId());
        Object userId = session.getAttribute("userId");

        if (userId == null) {
            log.info("[로그아웃 요청] 세션에는 로그인 정보 없음");
        } else {
            log.info("[로그아웃 요청] userId: {}", userId);
        }

        session.invalidate();

        log.info("[로그아웃 처리 완료] 세션 ID 무효화됨");
    }

    public ProfileResponseDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));

        return new ProfileResponseDto(user.getNickname(), user.getEmail());
    }

    public void updateProfile(Long userId, ProfileUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));


        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new DuplicateException("이미 존재하는 닉네임입니다.");
        }

        user.setNickname(request.getNickname());

        userRepository.save(user);
    }

    public String findLoginIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일로 등록된 유저가 없습니다."));

        return user.getLoginId();
    }

    public void resetPassword(String loginId, String email) {
        // 1. 사용자 검증
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("아이디가 존재하지 않습니다."));

        if (!user.getEmail().equals(email)) {
            throw new InvalidRequestException("아이디와 이메일이 일치하지 않습니다.");
        }

       /* // 2. 이메일 인증 여부 확인
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new InvalidRequestException("이메일 인증 정보가 없습니다."));

        if (!verification.isVerified()) {
            throw new InvalidRequestException("이메일 인증이 완료되지 않았습니다.");
        }

        if (verification.getExpiresAt() != null &&
                verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("이메일 인증이 만료되었습니다. 다시 인증해주세요.");
        }*/

        // 3. 임시 비밀번호 발급 및 저장
        String tempPassword = generateTempPassword(); // 6~8자리 숫자 or 문자열 생성
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        // 4. 이메일로 임시 비밀번호 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Kroum] 임시 비밀번호 안내");
        message.setText("임시 비밀번호: " + tempPassword + "\n로그인 후 반드시 비밀번호를 변경해주세요.");
        message.setFrom("4pril17@naver.com");

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendFailedException("임시 비밀번호 전송에 실패했습니다. 다시 시도해주세요.");
        }

        log.info("[임시 비밀번호 발급] loginId={}, tempPw={}", loginId, tempPassword);
    }


    public void changePassword(Long userId, PasswordChangeRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저 정보를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UnauthenticatedException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("해당 유저가 존재하지 않습니다.");
        }

        // 찜, 검색기록, 리뷰 등 먼저 삭제
        bookmarkRepository.deleteAllByUserId(userId);
        searchHistoryRepository.deleteAllByUserId(userId);
        reviewRepository.deleteAllByUserId(userId);
        // 필요한 연관 엔티티 다 삭제 후

        userRepository.deleteById(userId);
    }

    public MyPageResponseDto getMyPage(Long userId) {
        // 1. 프로필 정보
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저 정보를 찾을 수 없습니다."));
        ProfileResponseDto profile = new ProfileResponseDto(user.getNickname(), user.getEmail());

        // 2. 찜 목록 (북마크)
        List<Bookmark> bookmarks = bookmarkRepository.findByUser_Id(userId);
        List<BookmarkResponseDto> bookmarkDtos = bookmarks.stream()
                .map(b -> new BookmarkResponseDto(
                        b.getPlace().getPlaceId(),
                        b.getPlaceLanguage().getPlaceName(),
                        b.getCreatedAt().toLocalDate().toString(),
                        b.getPlace().getFirstImageUrl()
                ))
                .toList();

        // 3. 리뷰 요약
        List<ReviewSummaryResponseDto> reviewSummaries =
                reviewRepository.findReviewSummariesByUserId(userId);

        // 4. 응답 조립
        return new MyPageResponseDto(profile, bookmarkDtos, reviewSummaries);
    }



    private String generateTempPassword() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 8);
    }


    public PlaceDetailsByPlaceIdResponseDto getPlaceDetailsByPlaceId(Long placeId, HttpSession session) {
        Long userId = SessionUtil.getLoginUserId(session);

        // 1. 장소 기본 정보 (Projection → DTO 변환)
        PlaceDetailsProjection projection = placeLanguageRepository.findPlaceDetailsByPlaceId(placeId);

        // 2. 리뷰 정보
        Double avgRating = reviewRepository.findAverageRatingByPlaceId(placeId);
        double roundedAvg = (avgRating != null) ? Math.round(avgRating * 10.0) / 10.0 : 0.0;
        Long reviewCount = reviewRepository.countByPlace_PlaceId(placeId);
        List<PlaceReviewDto> reviews = reviewRepository.findPlaceReviewDtosByPlaceId(placeId);
        PlaceReviewsResponseDto reviewsDto = new PlaceReviewsResponseDto(reviewCount, roundedAvg, reviews);

        // 3. 찜 정보
        int bookmarkCount = bookmarkRepository.countByPlace_PlaceId(placeId);
        boolean isBookmarked = (userId != null) && bookmarkRepository.existsByUser_IdAndPlace_PlaceId(userId, placeId);
        PlaceBookmarkDto bookmarkDto = new PlaceBookmarkDto(bookmarkCount, isBookmarked);

        // 4. 조립
        PlaceDetailsResponseDto placeDetails = new PlaceDetailsResponseDto(reviewsDto, bookmarkDto);

        return PlaceDetailsByPlaceIdResponseDto.builder()
                .placeId(projection.getPlaceId())
                .placeName(projection.getPlaceName())
                .description(projection.getDescription())
                .address(projection.getAddress())
                .firstImageUrl(projection.getFirstImageUrl())
                .placeDetails(placeDetails)
                .bookmarked(isBookmarked)
                .build();
    }



    // encode 재사용 안 하는거 확정일 때 삭제
    /*public String hashPassword(String password) {
        password = passwordEncoder.encode(password);

        return password;
    }*/
}
