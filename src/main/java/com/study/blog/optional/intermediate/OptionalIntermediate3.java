package com.study.blog.optional.intermediate;

import java.util.Optional;

public class OptionalIntermediate3 {

    public static void main(String[] args) {
        // Optional 객체 생성
        Optional<String> optionalString = Optional.of("Example");

        // 여기서 map은 Optional<String>을 Optional<Optional<String>>으로 만든다.
        Optional<Optional<String>> wrapped = optionalString.map(Optional::of);

        // 여기서 flatMap은 Optional<Optional<String>>을 Optional<String>으로 만든다.
        Optional<String> flattened = wrapped.flatMap(o -> o);

        // 결과 출력
        flattened.ifPresent(System.out::println);  // "Example" 출력
    }
}
