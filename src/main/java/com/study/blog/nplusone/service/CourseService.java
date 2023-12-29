package com.study.blog.nplusone.service;

import com.study.blog.nplusone.entity.Course;
import com.study.blog.nplusone.entity.Student;
import com.study.blog.nplusone.repository.CourseRepository;
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

        List<Course> courses = courseRepository.findAll(); // N + 1이 발생하는 로직
//        List<Course> courses = courseRepository.findAllWithStudents(); // 엔티티 그래프 로직
//        List<Course> courses = courseRepository.findAllWithStudentsFetchJoin(); // Join Fetch 로직

        for (Course c : courses) {
            System.out.println("Course " + c.getName() + " has students: ");
            for (Student s : c.getStudents()) {
                System.out.println(" - " + s.getName());
            }
        }
    }

}