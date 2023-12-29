package com.study.blog.optional.intermediate;

import java.util.Optional;

public class OptionalIntermediate2 {

    public static void main(String[] args) {
        Optional<String> optionalString = Optional.of("Hello");

        // 문자열을 대문자로 변환
        Optional<String> upperString = optionalString.map(String::toUpperCase);

        upperString.ifPresent(System.out::println);  // "HELLO" 출력
    }
}
