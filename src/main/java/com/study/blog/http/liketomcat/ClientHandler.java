package com.study.blog.http.liketomcat;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ClientHandler 클래스 자체는 멀티스레딩을 구현한 것이 아니다.
 * 이 클래스는 Runnable 인터페이스를 구현하고 있고, 이 인터페이스의 run 메소드는 스레드가 실행할 작업을 정의하고 있다.
 * 멀티스레딩의 핵심은 이 클래스가 아니라 main 메서드 내부의 new Thread(clientHandler).start(); 이 부분에서 나타난다.
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {

            String line = reader.readLine();
            if (line == null) {
                return;
            }

            String[] requestLineParts = line.split(" ");
            String httpMethod = requestLineParts[0]; // http 종류
            String requestPath = requestLineParts[1]; // 요청 경로

            // 헤더값을 Map으로 만든 다음 세팅해 준다.
            HashMap<String, String> headerMap = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }

                String[] header = line.split(": ");
                headerMap.put(header[0], header[1]);
            }

            String responseBody = handleRequest(httpMethod, requestPath, reader, headerMap);
            String httpResponse = createHttpResponse(responseBody, headerMap);

            writer.write(httpResponse);
            writer.flush();

            if ("close".equalsIgnoreCase(headerMap.get("Connection"))) {
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 각 요청을 처리해둘 핸들러 메서드
    private String handleRequest(String httpMethod, String path, BufferedReader reader, Map<String, String> headers) throws IOException {
        // 요청 처리 로직
        if ("GET".equals(httpMethod)) {
            // 경로에 따라 다른 응답을 생성
            if ("/jmeter/test".equals(path)) {
                return "GET 요청에 대한 응답 : /test 경로";
            } else {
                // 다른 경로에 대한 기본 응답
                return "GET 요청에 대한 응답 : " + path;
            }
        } else if ("POST".equals(httpMethod) || "PUT".equals(httpMethod)) {
            return handlePostOrPutRequest(reader, headers);
        }
        return "Unsupported Method";
    }

    // post나 put 요청일때는 이 메서드를 사용한다.
    private String handlePostOrPutRequest(BufferedReader reader, Map<String, String> headerMap) throws IOException {

        // content의 length를 추출한다.
        int contentLength = Integer.parseInt(headerMap.getOrDefault("Content-Length", "0"));
        StringBuilder requestBody = new StringBuilder();

        // contentLength만큼 반복하여 body에 데이터를 쓴다.
        for (int i = 0; i < contentLength; i++) {
            requestBody.append((char) reader.read());
        }
        return "Received data: " + requestBody.toString();
    }

    // 인코딩을 utf-8로 설정한다.
    private String createHttpResponse(String responseBody, Map<String, String> headers) {
        // HTTP 응답 생성
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "Content-Type: text/plain; charset=UTF-8\r\n" +
                "\r\n" +
                responseBody;
    }

}
