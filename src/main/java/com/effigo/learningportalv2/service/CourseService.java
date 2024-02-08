package com.effigo.learningportalv2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.effigo.learningportalv2.controller.CourseController;
import com.effigo.learningportalv2.dto.CourseDto;
import com.effigo.learningportalv2.entity.Category;
import com.effigo.learningportalv2.entity.Course;
import com.effigo.learningportalv2.mapper.CourseMapper;
import com.effigo.learningportalv2.repository.CategoryRepository;
import com.effigo.learningportalv2.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	private final CourseRepository courseRepository;
	private final CategoryRepository categoryRepository;

	/**
	 * Constructor injection
	 * 
	 * @param courseRepository
	 * @param categoryRepository
	 */
	public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
		this.courseRepository = courseRepository;
		this.categoryRepository = categoryRepository;
	}

	/**
	 * fun : getting/search all courses
	 * 
	 * @return
	 */
	public List<CourseDto> searchAllCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses.stream().map(CourseMapper.INSTANCE::courseToCourseDto).collect(Collectors.toList());
	}

	/**
	 * fun : getting course by courseId
	 * 
	 * @param courseId
	 * @return
	 */
	public CourseDto getCourseById(String courseId) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

		return CourseMapper.INSTANCE.courseToCourseDto(course);
	}

	/**
	 * fun : create new course
	 * 
	 * @param courseDto
	 * @return
	 */
	public Course createCourse(Course course) {

		logger.info("Course " + course);
		Course savedCourse = courseRepository.save(course);

		return savedCourse;
	}

	/**
	 * fun : get all courses with particular category
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<CourseDto> getCoursesByCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));
		List<Course> courses = courseRepository.getCoursesByCategory(category);
		return courses.stream().map(CourseMapper.INSTANCE::courseToCourseDto).collect(Collectors.toList());

	}

	/**
	 * fun : update course
	 * 
	 * @param course
	 * @return
	 */
	@Transactional
	public Course updateCourse(Course course) {
		return courseRepository.save(course);
	}

}
