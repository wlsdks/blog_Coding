package com.study.blog.optional.basic;

import java.util.Optional;

public class OptionalBasics2 {

    public static void main(String[] args) {
        // null이 될 수 있는 객체를 Optional로 감싸기
        String nullString = null;
        Optional<String> nullableOptional = Optional.ofNullable(nullString);

        // 값이 없을 때 대체 텍스트 제공
        String result = nullableOptional.orElse("값이 없을때는 대체 텍스트가 제공된다.");

        System.out.println(result);  // "Default Text" 출력
    }
}