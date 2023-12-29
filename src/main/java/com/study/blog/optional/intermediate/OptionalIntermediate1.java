package com.study.blog.optional.intermediate;

import java.util.Optional;

public class OptionalIntermediate1 {

    public static void main(String[] args) {
        Optional<String> optionalString = Optional.of("옵셔널 생성");

        // 문자열의 길이가 5보다 큰 경우에만 값을 가져옴
        Optional<String> longString = optionalString.filter(s -> s.length() > 5);

        longString.ifPresent(System.out::println);  // "Hello World" 출력
    }
}
