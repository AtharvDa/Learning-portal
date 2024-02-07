package com.effigo.learningportalv2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effigo.learningportalv2.entity.Category;
import com.effigo.learningportalv2.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

	Optional<Course> findById(String courseId);

	List<Course> getCoursesByCategory(Category category);

}
