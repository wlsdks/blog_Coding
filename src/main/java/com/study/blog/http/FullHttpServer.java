package com.study.blog.http;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FullHttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                // 여기서 먼저 null 처리를 해줘야함
                String line = reader.readLine();
                if (line == null) {
                    continue; // 요청이 비어 있으면 다음 연결을 기다림
                }

                String[] requestLineParts = line.split(" ");
                String httpMethod = requestLineParts[0];
                String requestPath = requestLineParts[1];

                Map<String, String> headers = new HashMap<>();
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) break;

                    String[] header = line.split(": ");
                    headers.put(header[0], header[1]);
                }

                // 헤더 정보 출력
                System.out.println("HTTP Headers:");
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    System.out.println(header.getKey() + ": " + header.getValue());
                }

                String responseBody = "";
                if ("GET".equals(httpMethod)) {
                    responseBody = handleGetRequest(requestPath);
                } else if ("POST".equals(httpMethod)) {
                    responseBody = handlePostOrPutRequest(reader, headers);
                } else if ("PUT".equals(httpMethod)) {
                    responseBody = handlePostOrPutRequest(reader, headers);
                }

                // 인코딩을 추가해줘야 한국어가 잘 받아진다.
                String httpResponse = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                        "Content-Type: text/plain; charset=UTF-8\r\n" +
                        "\r\n" +
                        responseBody;

                writer.write(httpResponse);
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String handleGetRequest(String path) {
        // GET 요청 처리 로직 (한국어를 넣으면 postman이 응답을 못함.. 이건 또 뭐야)
        return "GET 요청에 대한 응답 : " + path;
    }

    private static String handlePostOrPutRequest(BufferedReader reader, Map<String, String> headers) throws IOException {
        // POST와 PUT 요청 본문 처리 로직
        int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
        StringBuilder requestBody = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            requestBody.append((char) reader.read());
        }
        return "Received data: " + requestBody.toString();
    }
}
