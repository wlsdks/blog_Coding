package com.study.blog.repository;

import com.study.blog.entity.Course;

import java.util.List;

public interface CourseRepositoryCustom {
    List<Course> findAllWithStudentsFetchJoin();
}
