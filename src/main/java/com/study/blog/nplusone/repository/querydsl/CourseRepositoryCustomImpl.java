package com.study.blog.nplusone.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.blog.nplusone.entity.Course;
import com.study.blog.nplusone.entity.QCourse;
import com.study.blog.nplusone.repository.CourseRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CourseRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Course> findAllWithStudentsFetchJoin() {
        QCourse course = QCourse.course;

        return queryFactory
                .selectFrom(course)
                .leftJoin(course.students).fetchJoin()
                .fetch();
    }
}
