package com.kroum.kroum.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class SearchInputAnalyzer {

    private static final Set<String> NEGATIVE_WORDS = Set.of(
            "싫", "싫다", "싫어", "별로", "맛없", "안좋", "최악", "실망", "더러", "거지같", "구림", "별로임", "비추", "개노잼", "노잼", "안 가", "안감", "가지 마", "비싸", "돈 아깝"
    );

    private static final Pattern NOISE_PATTERN = Pattern.compile("[ㅋㅎㅠㅜㅡ]+|[!?~]+");

    /**
     * 의미 없는 글자 제거
     */
    public String sanitize(String input) {
        if (input == null) return "";
        return NOISE_PATTERN.matcher(input).replaceAll("").trim();
    }

    /**
     * 부정 표현 포함 여부 확인
     */
    public boolean containsNegativeExpression(String input) {
        if (input == null) return false;
        return NEGATIVE_WORDS.stream().anyMatch(input::contains);
    }

    /**
     * 처리 불가능한 문장인지 확인
     */
    public boolean isBadSentence(String input) {
        String cleaned = sanitize(input);
        return cleaned.isBlank() || containsNegativeExpression(cleaned);
    }
}

