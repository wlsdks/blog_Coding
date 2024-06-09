package com.study.blog.http.liketomcat;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Router {

    private final Map<Pattern, RouteHandler> routes = new HashMap<>();

    public void addRoute(String path, RouteHandler handler) {
        Pattern pattern = Pattern.compile(path);
        routes.put(pattern, handler);
    }

    public RouteHandler getHandler(String path) {
        return routes.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(path).matches())
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

}
