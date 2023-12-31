package com.study.blog.http.threadpool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerSocket을 사용하여 클라이언트의 연결을 기다린다.
 * 연결이 수립되면 CustomThreadPool을 사용하여 ClientHandler 인스턴스를 실행한다.
 */
public class ThreadPoolMultiThreadHttpServer {

    public static void main(String[] args) {
        CustomThreadPool customThreadPool = new CustomThreadPool(
                10,  // corePoolSize
                20,  // maximumPoolSize
                5000,// keepAliveTime
                100  // queueCapacity
        );

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Listening for connection on port 8080 ....");

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    customThreadPool.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            customThreadPool.shutdown();
        }
    }
}
