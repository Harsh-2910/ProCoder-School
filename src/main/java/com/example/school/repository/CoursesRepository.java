package com.example.school.repository;

import com.example.school.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported=false)
public interface CoursesRepository extends JpaRepository<Courses, Integer> {
    //asc order
    List<Courses> findByOrderByName();
    //desc order
    List<Courses> findByOrderByNameDesc();
}
