package com.study.blog.repository;

import com.study.blog.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // 추가적인 쿼리 메소드가 필요하면 여기에 정의
}
