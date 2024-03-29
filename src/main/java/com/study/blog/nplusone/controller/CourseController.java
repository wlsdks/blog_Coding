package com.study.blog.nplusone.controller;

import com.study.blog.nplusone.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/print")
    public void printCoursesWithStudents() {
        courseService.printAllCoursesWithStudents();
    }

}
