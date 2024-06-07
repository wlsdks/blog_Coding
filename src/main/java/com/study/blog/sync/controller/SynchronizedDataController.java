package com.study.blog.sync.controller;

import com.study.blog.sync.service.SyncTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 데이터 동기화(synchronized)를 적용한 코드
 */
@RequestMapping("/synchronized-data")
@RequiredArgsConstructor
@RestController
public class SynchronizedDataController {

    private final SyncTestService syncTestService;

    @PostMapping("/add")
    public synchronized ResponseEntity<Void> addData(@RequestBody String data) {
        syncTestService.addData(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/increment")
    public synchronized ResponseEntity<Void> incrementCounter(@RequestParam Long id) {
        syncTestService.incrementCounter(id);
        return ResponseEntity.ok().build();
    }

}
