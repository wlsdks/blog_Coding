package com.study.blog.optional.basic;

import java.util.Optional;

public class OptionalBasics {

    public static void main(String[] args) {
        // Optional 객체 생성
        Optional<String> helloOptional = Optional.of("Optional 객체를 생성한다.");

        // Optional에서 값을 가져오기 - 값이 확실히 존재한다고 가정
        String result = helloOptional.get();

        System.out.println(result);  // "Hello World" 출력
    }

}
