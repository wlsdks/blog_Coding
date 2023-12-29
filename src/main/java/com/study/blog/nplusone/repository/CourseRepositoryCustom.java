package com.study.blog.nplusone.repository;

import com.study.blog.nplusone.entity.Course;

import java.util.List;

public interface CourseRepositoryCustom {
    List<Course> findAllWithStudentsFetchJoin();
}
