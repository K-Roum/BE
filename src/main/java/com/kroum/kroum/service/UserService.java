package com.kroum.kroum.service;

import lombok.extern.slf4j.Slf4j;

import com.kroum.kroum.dto.request.LoginRequestDto;
import com.kroum.kroum.dto.request.SignupRequestDto;
import com.kroum.kroum.entity.User;
import com.kroum.kroum.exception.*;
import com.kroum.kroum.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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



    // encode 재사용 안 하는거 확정일 때 삭제
    /*public String hashPassword(String password) {
        password = passwordEncoder.encode(password);

        return password;
    }*/
}
