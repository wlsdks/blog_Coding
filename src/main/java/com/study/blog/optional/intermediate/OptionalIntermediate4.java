package com.study.blog.optional.intermediate;

import java.util.Optional;

public class OptionalIntermediate4 {
    public static void main(String[] args) {
        Optional<String> firstName = Optional.empty();
        Optional<String> lastName = Optional.of("Jin");

        // 이름과 성을 조합하여 전체 이름 만들기
        String fullName = firstName.flatMap(fName -> lastName.map(lName -> fName + " " + lName))
                .orElse(firstName.orElse("Unknown"));

        System.out.println(fullName);
    }
}

