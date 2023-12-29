package com.study.blog.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * HttpClientExample (클라이언트 측)
 *
 * 이 코드는 자바 11에서 도입된 HttpClient를 사용하여 HTTP 요청을 보내는 클라이언트를 구현해.
 * 클라이언트는 특정 URL(http://example.com)로 GET 요청을 보내고, 서버로부터 받은 응답을 처리해.
 * 이 클라이언트는 서버와의 통신을 위해 HTTP 요청을 구성하고, 응답을 받아 그 내용을 출력해.
 */
public class HttpClientExample {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080"))
                .GET() // GET 요청
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}

