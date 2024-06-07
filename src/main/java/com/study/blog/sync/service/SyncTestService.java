package com.study.blog.sync.service;

import com.study.blog.sync.entity.SyncTestEntity;
import com.study.blog.sync.repository.SyncTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SyncTestService {

    private final SyncTestRepository syncTestRepository;

    /**
     * 데이터를 저장한다.
     */
    @Transactional
    public void addData(String data) {
        SyncTestEntity entity = SyncTestEntity.of(data);
        syncTestRepository.save(entity);
    }

    /**
     * synchronized 키워드를 사용하여 id에 해당하는 데이터의 counter를 1 증가시킨다.
     */
    @Transactional
    public void syncIncrementCounter(Long id) {
        synchronized (this) {
            SyncTestEntity entity = syncTestRepository.findById(id).orElseThrow();
            entity.incrementCounter();
            syncTestRepository.save(entity);
        }
    }

    /**
     * id에 해당하는 데이터의 counter를 1 증가시킨다.
     */
    @Transactional
    public void incrementCounter(Long id) {
        SyncTestEntity entity = syncTestRepository.findById(id).orElseThrow();
        entity.incrementCounter();
        syncTestRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<SyncTestEntity> getAllData() {
        return syncTestRepository.findAll();
    }

}
