package com.study.blog.service;

import com.study.blog.domain.Course;
import com.study.blog.domain.Student;
import com.study.blog.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public void printAllCoursesWithStudents() {

        List<Course> courses = courseRepository.findAll();

        for (Course c : courses) {
            System.out.println("Course " + c.getName() + " has students: ");
            for (Student s : c.getStudents()) {
                System.out.println(" - " + s.getName());
            }
        }
    }

}