package com.study.blog.nplusone.repository;

import com.study.blog.nplusone.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> , CourseRepositoryCustom{
    @EntityGraph(attributePaths = {"students"})
    @Query("SELECT c FROM Course c")
    List<Course> findAllWithStudents();
}