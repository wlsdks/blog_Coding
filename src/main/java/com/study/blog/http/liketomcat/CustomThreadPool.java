package com.study.blog.http.liketomcat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Java의 ThreadPoolExecutor를 사용하여 커스텀 스레드 풀을 구현했다.
 * 여기서 스레드 풀의 핵심 매개변수들(핵심 스레드 수, 최대 스레드 수, 유휴 시간, 큐 용량)을 설정한다.
 */
public class CustomThreadPool {
    private ThreadPoolExecutor threadPoolExecutor;

    // 스레드 풀 초기화
    public CustomThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, int queueCapacity) {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy() // 작업 거부 정책
        );
    }

    public void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    // 필요한 경우 다른 ThreadPoolExecutor 메서드 추가
}
