package com.study.blog.optional.basic;

import java.util.Optional;

public class OptionalBasics4 {

    public static void main(String[] args) {
        Optional<String> emptyOptional = Optional.empty();

        // orElse - 값이 없을 경우 기본 문자열 반환
        String result = emptyOptional.orElse("값이 없으면 여기서 문자열 생성");
        System.out.println(result);  // "Default Value" 출력

        // orElseGet - 값이 없을 경우 람다식을 통해 값을 계산하여 반환
        String computedResult = emptyOptional.orElseGet(() -> "값이 없으면 람다식으로 값을 생성");
        System.out.println(computedResult);  // "Computed Value" 출력
    }
}
