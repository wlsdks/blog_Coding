package com.study.blog.sync.entity;

import com.study.blog.sync.repository.SyncTestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SyncTestEntityTest {

    @Autowired
    private SyncTestRepository syncTestRepository;

    @DisplayName("factory method로 엔티티를 생성할때 메서드 내부에서 id를 null로 설정해도 엔티티가 정상적으로 생성되는지와 테이블에 id가 정상적으로 생성되는지 확인")
    @Test
    void testEntityCreation() {
        //given
        SyncTestEntity entity = SyncTestEntity.of("test data");

        //when
        SyncTestEntity savedEntity = syncTestRepository.save(entity);

        //then
        Assertions.assertThat(savedEntity.getId()).isNotNull();
        Assertions.assertThat(savedEntity.getData()).isEqualTo("test data");
    }

    @DisplayName("factory method로 엔티티를 여러개 생성해서 저장할때 메서드 내부에서 id를 null로 설정해도 테이블에 id가 정상적으로 증가하여 저장되는지 확인")
    @Test
    void testEntityCreation2() {
        //given
        SyncTestEntity entity1 = SyncTestEntity.of("test data1");
        SyncTestEntity entity2 = SyncTestEntity.of("test data2");

        //when
        SyncTestEntity savedEntity1 = syncTestRepository.save(entity1);
        SyncTestEntity savedEntity2 = syncTestRepository.save(entity2);

        //then
        Assertions.assertThat(savedEntity1.getId()).isNotNull();
        Assertions.assertThat(savedEntity2.getId()).isNotNull();

        Assertions.assertThat(savedEntity1.getData()).isEqualTo("test data1");
        Assertions.assertThat(savedEntity2.getData()).isEqualTo("test data2");

        // id가 generatedValue로 생성되는 경우 id값이 서로 다르게 생성되는지 확인
        Assertions.assertThat(savedEntity2.getId()).isNotEqualTo(savedEntity1.getId());
        Assertions.assertThat(savedEntity2.getId()).isGreaterThan(savedEntity1.getId());
    }

}