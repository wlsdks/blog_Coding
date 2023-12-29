package com.study.blog.optional.basic;

import java.util.Optional;

public class OptionalBasics3 {

    public static void main(String[] args) {
        Optional<String> maybeData = Optional.of("값이 존재하도록 설정한다.");

        // isPresent를 사용하여 값이 존재하는지 확인
        if (maybeData.isPresent()) {
            System.out.println(maybeData.get());  // 값 출력
        }

        // ifPresent를 사용하여 람다식을 통해 값이 있을 때만 작업 실행
        maybeData.ifPresent(data -> System.out.println("Lambda: " + data));  // "Lambda: Hello Optional" 출력
    }
}