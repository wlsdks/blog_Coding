package com.study.blog.sync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.blog.sync.entity.SyncTestEntity;
import com.study.blog.sync.repository.SyncTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SyncTestRepository repository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        repository.deleteAll();
        mockMvc.perform(post("/synchronized-data/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("InitialData")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("controller에서 동기화 제어를 하지 않을때 카운터 증가 - 카운터 값이 올바르지 않아야 함")
    public void testIncrementCounterWithoutSynchronization() throws Exception {
        // Given -> 테스트에 사용할 스레드 수와 각 스레드가 실행할 반복 횟수를 설정한다.
        int threadCount = 10;  // 총 10개의 스레드를 생성
        int iterations = 200;  // 각 스레드는 200번의 요청을 보내도록 설정

        // CountDownLatch는 모든 스레드가 작업을 완료할 때까지 대기하는 데 사용된다.
        // 초기 카운트를 스레드 수와 동일하게 설정하여 설정해준 모든 스레드가 작업을 완료할 때까지 대기할 수 있도록 한다. (10개의 스레드)
        CountDownLatch latch = new CountDownLatch(threadCount);

        // ExecutorService는 스레드 풀을 관리한다. 테스트를 위해 고정된 크기의 스레드 풀을 생성한다. (10개의 스레드)
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 각 스레드가 실행할 작업을 정의한다.
        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                try {
                    // /not-synchronized-data/increment 엔드포인트에 요청을 보낸다.
                    // 이 엔드포인트는 컨트롤러에서 동기화(synchronized)되지 않은 상태에서 카운터를 증가시킨다.
                    mockMvc.perform(post("/not-synchronized-data/increment")
                                    .param("id", "1"))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 각 스레드가 작업을 완료할 때마다 호출하여 카운트를 1씩 감소시킨다. 만약 모든 스레드가 작업을 완료하면 카운트는 0이 된다.
            latch.countDown();
        };

        // When -> 스레드 풀을 사용하여 위에서 정의한 작업(엔드포인트에 요청 보내기)을 실행한다.
        for (int i = 0; i < threadCount; i++) {
            executor.execute(task);
        }

        // CountDownLatch를 사용하여 모든 스레드가 작업을 완료할 때까지(카운트가 0이 될 때까지) 대기한다.
        // 즉, 모든 스레드가 작업을 완료할 때까지 메인 스레드가 대기한다.
        latch.await();

        // 스레드 풀을 종료하고 모든 스레드가 종료될 때까지 기다린다.
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Then
        // 데이터베이스에서 엔티티를 조회하고 카운터 값이 올바르게 증가하지 않았음을 확인한다.
        SyncTestEntity entity = repository.findById(1L).orElseThrow();

        // 동기화가 되지 않아 올바르게 증가하지 않을 가능성이 높다.
        assertThat(entity.getCounter()).isNotEqualTo(threadCount * iterations);
        System.out.println("Counter without synchronization: " + entity.getCounter());
    }

    @Test
    @DisplayName("controller에서 동기화 제어를 하면서 카운터 증가 - 카운터 값이 올바르게 증가해야 함")
    public void testIncrementCounterWithSynchronization() throws Exception {
        // Given
        // 테스트에 사용할 스레드 수와 각 스레드가 실행할 반복 횟수를 설정합니다.
        int threadCount = 10;  // 총 10개의 스레드를 생성합니다.
        int iterations = 200;  // 각 스레드는 200번의 요청을 보냅니다.

        // CountDownLatch는 모든 스레드가 작업을 완료할 때까지 대기하는 데 사용됩니다.
        CountDownLatch latch = new CountDownLatch(threadCount);

        // ExecutorService는 스레드 풀을 관리합니다. 여기서는 고정된 크기의 스레드 풀을 생성합니다.
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 각 스레드가 실행할 작업을 정의합니다.
        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                try {
                    // /synchronized-data/increment 엔드포인트에 요청을 보냅니다.
                    // 이 엔드포인트는 동기화된 상태에서 카운터를 증가시킵니다.
                    mockMvc.perform(post("/synchronized-data/increment")
                                    .param("id", "1"))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 작업이 완료되면 CountDownLatch의 카운터를 감소시킵니다.
            latch.countDown();
        };

        // When
        // 스레드 풀을 사용하여 작업을 실행합니다.
        for (int i = 0; i < threadCount; i++) {
            executor.execute(task);
        }

        // CountDownLatch를 사용하여 모든 스레드가 작업을 완료할 때까지 대기합니다.
        latch.await();
        // 스레드 풀을 종료하고 모든 스레드가 종료될 때까지 기다립니다.
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Then
        // 데이터베이스에서 엔티티를 조회하고 카운터 값이 올바르게 증가했음을 확인합니다.
        SyncTestEntity entity = repository.findById(1L).orElseThrow();
        assertThat(entity.getCounter()).isEqualTo(threadCount * iterations);  // 동기화가 되어 올바르게 증가해야 함
        System.out.println("Counter with synchronization: " + entity.getCounter());
    }

}