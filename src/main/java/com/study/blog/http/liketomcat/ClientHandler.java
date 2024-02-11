package com.study.blog.http.liketomcat;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * ClientHandler 클래스 자체는 멀티스레딩을 구현한 것이 아니다.
 * 이 클래스는 Runnable 인터페이스를 구현하고 있고, 이 인터페이스의 run 메소드는 스레드가 실행할 작업을 정의하고 있다.
 * 멀티스레딩의 핵심은 이 클래스가 아니라 main 메서드 내부의 new Thread(clientHandler).start(); 이 부분에서 나타난다.
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private final Gson gson = new Gson(); // JSON 처리를 위한 GSON 선언
    private Map<String, Function<Map<String, String>, String>> routeHandlers; // 라우팅 핸들러

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        initializeRoutes();
    }

    /**
     * 라우팅 로직 추가
     */
    private void initializeRoutes() {
        routeHandlers = new HashMap<>();
        // 여기에 경로별 핸들러를 추가
        routeHandlers.put("/test/jinan", this::jinanHandler);
    }

    /**
     * Json 응답 처리
     */
    private String jinanHandler(Map<String, String> headers) {
        // '/test/jinan' 경로의 핸들러 로직, JSON 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Welcome to /test/jinan");
        response.put("data", new HashMap<String, String>() {{
            put("detail", "This is the detailed information for /test/jinan.");
        }});
        return gson.toJson(response); // Map을 JSON 문자열로 변환
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
            String requestPath = requestLineParts[1]; // 요청 경로

            // 헤더 처리
            HashMap<String, String> headerMap = new HashMap<>();
            while (!(line = reader.readLine()).isEmpty()) {
                String[] headerParts = line.split(": ");
                headerMap.put(headerParts[0], headerParts[1]);
            }

            // 요청 경로에 따른 핸들러 호출
            String response;
            if (routeHandlers.containsKey(requestPath)) {
                response = routeHandlers.get(requestPath).apply(headerMap);
            } else {
                response = gson.toJson(Map.of("error", "404 Not Found"));
            }

            // HTTP 응답 생성 및 전송
            String httpResponse = createHttpResponse(response, headerMap, 200);// 상태코드는 예시로 200 사용
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

    /**
     * 클라이언트에게 보낼 HTTP 응답을 생성한다.
     */
    private String createHttpResponse(String responseBody, Map<String, String> headers, int statusCode) {
        String statusLine = "HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode) + "\r\n";
        StringBuilder responseBuilder = new StringBuilder(statusLine);

        // 기본 헤더 추가
        responseBuilder.append("Content-Type: application/json; charset=UTF-8\r\n");
        responseBuilder.append("Content-Length: ").append(responseBody.getBytes(StandardCharsets.UTF_8).length).append("\r\n");

        // 사용자 정의 헤더 추가 (옵션)
        for (Map.Entry<String, String> header : headers.entrySet()) {
            responseBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        responseBuilder.append("\r\n").append(responseBody); // 응답 본문

        return responseBuilder.toString();
    }



    // HTTP 상태 코드에 대한 이유 구문을 반환하는 메소드
    private String getReasonPhrase(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            // 다른 상태 코드 처리
            default: return "";
        }
    }

}
