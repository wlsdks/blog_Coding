package com.study.blog.requestbody.controller;

import com.study.blog.requestbody.dto.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RequestBodyTestController {

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/requestbody")
    public ResponseEntity<TestDto> requestBody(@RequestBody TestDto dto) {
        log.info("dto = {}", dto);
        return ResponseEntity.ok(dto);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/requestbody")
    public ResponseEntity<TestDto> requestBodyGet(TestDto dto) {
        log.info("dto = {}", dto);
        return ResponseEntity.ok(dto);
    }

}
