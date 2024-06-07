package com.study.blog.sync.controller;

import com.study.blog.sync.service.SyncTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 데이터 동기화(synchronized)를 적용하지 않은 코드
 */
@RequestMapping("/not-synchronized-data")
@RequiredArgsConstructor
@RestController
public class NotSynchronizedDataController {

    private final SyncTestService syncTestService;

    @PostMapping("/add")
    public ResponseEntity<Void> addData(@RequestBody String data) {
        syncTestService.addData(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/increment")
    public ResponseEntity<Void> incrementCounter(@RequestParam Long id) {
        syncTestService.syncIncrementCounter(id);
        return ResponseEntity.ok().build();
    }

}
