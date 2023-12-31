package com.study.blog.http.multithread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadHttpServer {

    /**
     * ServerSocket은 IOException을 발생시킬 수 있다. 이는 네트워크 문제, 포트 충돌, 서버 소켓이 예상치 못하게 닫히는 경우 등 다양한 이유로 발생한다.
     * 그래서 accept 메소드 호출을 try-catch 블록으로 감싸서, 발생 가능한 IOException을 적절히 처리해야 한다.
     *
     */
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Listening for connection on port 8080 ....");

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    // 여기서 IOException을 처리
                    System.err.println("Error accepting client connection: " + e.getMessage());
                    // 필요한 경우 루프를 끝내거나 서버를 재시작할 수 있어.
                }
            }
        } catch (IOException e) {
            // ServerSocket 생성 중 발생한 예외 처리
            System.err.println("Cannot start server: " + e.getMessage());
        }
    }

}
