package com.study.blog.http;

import java.io.*;
import java.net.*;

/**
 * SimpleHttpServer (서버 측)
 *
 * 이 코드는 자바의 ServerSocket을 사용하여 간단한 HTTP 서버를 구현해.
 * 서버는 8080 포트에서 클라이언트의 연결을 기다리고, 요청을 받으면 이를 처리한 후 "Hello World!"라는 응답을 보내.
 * 이 서버는 HTTP 프로토콜의 기본적인 개념을 이해하고 있어야 하며, 클라이언트의 요청을 직접 파싱하고 응답을 구성해야 해.
 * http://127.0.0.1:8080 -> 이렇게 요청해야 동작함
 */
public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Connection accepted: " + clientSocket);

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                StringBuilder requestBuilder = new StringBuilder();
                String line;

                // 요청 헤더 읽기
                line = reader.readLine();
                while (!line.isEmpty()) {
                    requestBuilder.append(line).append("\n");
                    line = reader.readLine();
                }

                // 요청 헤더 출력
                System.out.println("Request Header: \n" + requestBuilder.toString());

                // HTTP 응답 생성 및 전송
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "Hello World!";
                clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
