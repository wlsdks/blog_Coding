package com.study.blog.sync.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class SyncTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;

    private int counter;

    // 펙토리 메서드 생성을 위해 생성자는 private 선언
    private SyncTestEntity(Long id, String data) {
        this.id = id;
        this.data = data;
        this.counter = 0;
    }

    // factory method 선언
    public static SyncTestEntity of(String data) {
        return new SyncTestEntity(null, data);
    }

    // counter 증가 메서드
    public void incrementCounter() {
        this.counter++;
    }
}
