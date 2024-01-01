package com.study.blog.http.liketomcat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Java의 ThreadPoolExecutor를 사용하여 커스텀 스레드 풀을 구현했다.
 * 여기서 스레드 풀의 핵심 매개변수들(핵심 스레드 수, 최대 스레드 수, 유휴 시간, 큐 용량)을 설정한다.
 */
public class CustomThreadPool {
    private ThreadPoolExecutor threadPoolExecutor;

    public CustomThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, int queueCapacity) {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,         // 핵심 스레드 수: 이 값은 스레드 풀이 항상 유지하는 스레드 수를 결정한다.
                maximumPoolSize,      // 최대 스레드 수: 스레드 풀이 동시에 실행할 수 있는 최대 스레드 수를 지정한다.
                keepAliveTime,        // 유휴 스레드 유지 시간: 추가 스레드(핵심 스레드를 제외한 스레드)가 유휴 상태로 유지될 수 있는 시간을 결정한다.
                TimeUnit.MILLISECONDS,// 시간 단위 : 스레드 풀에 들어오는 작업이 바로 처리될 수 없을 때 대기하는 큐의 크기를 결정한다.
                new ArrayBlockingQueue<>(queueCapacity) // 작업 대기열
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
