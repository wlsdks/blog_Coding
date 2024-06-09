package com.study.blog.http.liketomcat;

import java.util.Map;

public interface RouteHandler {

    String handle(Map<String, String> headers, Map<String, String> params);

}
