package com.effigo.learningportalv2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effigo.learningportalv2.entity.Course;
import com.effigo.learningportalv2.entity.Enrollment;
import com.effigo.learningportalv2.entity.User;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

	List<Enrollment> findByUser(User user);

	boolean existsByUserAndCourse(User user, Course course);

}
