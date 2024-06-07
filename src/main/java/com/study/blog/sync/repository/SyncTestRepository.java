package com.study.blog.sync.repository;

import com.study.blog.sync.entity.SyncTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncTestRepository extends JpaRepository<SyncTestEntity, Long> {
}
