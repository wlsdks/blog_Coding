package com.study.blog.nplusone.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NamedEntityGraph(name = "Course.students", attributeNodes = @NamedAttributeNode("students"))
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 일대다 관계 정의: Course는 여러 Student를 가질 수 있음
//    @BatchSize(size = 10)
    @OneToMany(mappedBy = "course")
    private Set<Student> students = new HashSet<>();

}
