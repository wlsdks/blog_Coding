package com.study.blog.requestbody.controller;

import com.study.blog.requestbody.dto.TestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestBodyTestController {

    @PostMapping("/requestbody")
    public ResponseEntity<TestDto> requestBody(@RequestBody TestDto dto) {
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/requestbody")
    public ResponseEntity<TestDto> requestBodyGet(TestDto dto) {
        return ResponseEntity.ok(dto);
    }

}
