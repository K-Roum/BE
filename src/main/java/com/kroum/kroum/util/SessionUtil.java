package com.kroum.kroum.util;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static Long getLoginUserId(HttpSession session) {
        if (session == null) return null;

        return (Long) session.getAttribute("userId");
    }

    public static Long requireLoginUserId(HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return userId;
    }

}
