package com.study.blog.http.liketomcat;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    private static final String WEB_ROOT = "src/main/resources"; // 정적 파일이 위치한 디렉토리

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        initializeRoutes();
    }

    /**
     * 라우팅 로직 추가
     * initializeRoutes() 메서드는 routeHandlers 맵에 경로별 핸들러를 사전에 설정해두는 역할만 수행하며,
     * 실제 경로에 대한 요청이 처리되는 것은 클라이언트로부터 해당 경로에 대한 HTTP 요청이 서버에 도착했을 때다.
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

    /**
     * 정적 파일을 처리한다.
     */
    public void serveStaticFile(String filePath, BufferedWriter writer) throws IOException {
        File file = new File(WEB_ROOT, filePath);
        if (file.exists() && !file.isDirectory()) {
            // 파일 내용을 읽어와서 클라이언트에게 전송
            String contentType = Files.probeContentType(file.toPath());
            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Content-Type: " + contentType + "\r\n");
            writer.write("Content-length: " + file.length() + "\r\n");
            writer.write("\r\n");
            writer.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    writer.write(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
                }
            }
            writer.flush();
        } else {
            // 파일이 없는 경우 404 응답
            writer.write("HTTP/1.1 404 Not Found\r\n");
            writer.write("\r\n");
            writer.write("404 Not Found");
            writer.flush();
        }
    }

    /**
     *  클라이언트가 예를 들어 curl 명령어를 사용하여 http://서버주소/test/jinan 경로로 요청을 보내면,
     *  서버는 ClientHandler의 run() 메소드를 실행하여 이 요청을 처리한다.
     */
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

            // 클라이언트로부터 요청 라인을 읽어 requestPath 변수에 요청된 경로를 저장한다.
            String[] requestLineParts = line.split(" ");
            String httpMethod = requestLineParts[0];
            String requestPath = requestLineParts[1];

            /**
             * 정적 파일 요청 처리
             * /static/으로 시작하는 경로로 들어오는 요청을 정적 파일 요청으로 간주하고, 요청된 파일을 WEB_ROOT 디렉토리에서 찾아 반환한다.
             * 파일이 존재하지 않는 경우 404 Not Found 응답을 반환한다.
             */
            if (httpMethod.equals("GET") && requestPath.startsWith("/static/")) {
                String filePath = requestPath.substring("/static/".length()); // "/static/" 경로 제거
                serveStaticFile(filePath, writer);
                return; // 정적 파일 처리 후 메서드 종료
            }

            // 요청 헤더들을 읽어 headerMap에 저장한다. 이 맵은 요청에 포함된 헤더들의 이름과 값을 저장한다.
            HashMap<String, String> headerMap = new HashMap<>();
            while (!(line = reader.readLine()).isEmpty()) {
                String[] headerParts = line.split(": ");
                headerMap.put(headerParts[0], headerParts[1]);
            }

            // routeHandlers 맵에서 requestPath에 해당하는 핸들러를 찾는다.
            // 만약 핸들러가 존재한다면, 해당 핸들러 함수를 호출하여 요청을 처리하고 응답 본문을 생성한다.
            String response;
            if (routeHandlers.containsKey(requestPath)) {
                response = routeHandlers.get(requestPath).apply(headerMap);
            } else {
                response = gson.toJson(Map.of("error", "404 Not Found"));
            }

            // 생성된 응답 본문을 포함한 HTTP 응답 메시지를 클라이언트에게 전송하고 연결을 종료한다.
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
