package com.effigo.learningportalv2.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.effigo.learningportalv2.dto.CourseDto;
import com.effigo.learningportalv2.dto.UserDto;
import com.effigo.learningportalv2.entity.Category;
import com.effigo.learningportalv2.entity.Course;
import com.effigo.learningportalv2.entity.User;
import com.effigo.learningportalv2.enums.UserRole;
import com.effigo.learningportalv2.mapper.CategoryMapper;
import com.effigo.learningportalv2.mapper.CourseMapper;
import com.effigo.learningportalv2.mapper.UserMapper;
import com.effigo.learningportalv2.service.CategoryService;
import com.effigo.learningportalv2.service.CourseService;
import com.effigo.learningportalv2.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

	private final CourseService courseService;
	private final UserService userService;
	private final CategoryService categoryService;

	public CourseController(CourseService courseService, UserService userService, CategoryService categoryService) {
		this.courseService = courseService;
		this.userService = userService;
		this.categoryService = categoryService;
	}

	@GetMapping("search")
	public ResponseEntity<Object> searchAllCourses() {
		logger.info("Fetching all courses");
		List<CourseDto> courses = courseService.searchAllCourses();
		logger.info("Fetched course: {}", courses);
		return ResponseEntity.ok(courses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CourseDto> getCourseById(@PathVariable String id) {
		logger.info("Fetching course with ID: {}", id);
		try {
			CourseDto courseDto = courseService.getCourseById(id);
			logger.info("Fetched course: {}", courseDto);
			return ResponseEntity.ok(courseDto);
		} catch (EntityNotFoundException ex) {
			logger.error("Course not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/author")
	public ResponseEntity<Object> createCourse(@RequestBody CourseDto courseDto, @RequestParam Long authorId,
			@RequestParam Long categoryId) {
		logger.info("Creating course for author ID: {}, category ID: {}", authorId, categoryId);
		try {
			// Basic role check - Only authors can create courses
			if (isUserAuthor(authorId)) {
				// Fetch the category by ID
				logger.info("Fetching category by ID: {}", categoryId);
				Category category = CategoryMapper.INSTANCE
						.categoryDtoToCategory(categoryService.getCategoryById(categoryId));
				if (category == null) {
					logger.error("Invalid category ID: {}", categoryId);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category ID");
				}

				Course course = CourseMapper.INSTANCE.courseDtoToCourse(courseDto);
				logger.info("course required : {}", course);

//				Set<Course> courses = category.getCourses();
//				courses.add(course);
//				category.setCourses(courses);

				User author = UserMapper.INSTANCE.userDtoToUser(userService.getUserById(authorId));
				logger.info("Author required : {}", author);
				course.setAuthor(author);
				course.setCategory(category);
				logger.info("course required : {}", course);

				Course createdCourse = courseService.createCourse(course);
				CourseDto createdCourseDto = CourseMapper.INSTANCE.courseToCourseDto(createdCourse);

				return ResponseEntity.status(HttpStatus.CREATED).body(createdCourseDto);
			} else {
				logger.warn("Unauthorized access to create course by user ID: {}", authorId);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception ex) {
			logger.error("An error occurred while creating course: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/author")
	public ResponseEntity<Object> editCourse(@RequestParam String courseId, @RequestBody CourseDto courseDto) {
		logger.info("Editing course with ID: {}", courseId);
		try {
			Course existingCourse = CourseMapper.INSTANCE.courseDtoToCourse(courseService.getCourseById(courseId));
			if (existingCourse == null) {
				logger.error("Course not found with ID: {}", courseId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with ID: " + courseId);
			}

			// Update course details
			existingCourse.setTitle(courseDto.getTitle());
			existingCourse.setPrice(courseDto.getPrice());
			// Update other fields as needed

			Course updatedCourse = courseService.updateCourse(existingCourse);
			CourseDto updatedCourseDto = CourseMapper.INSTANCE.courseToCourseDto(updatedCourse);

			logger.info("Course updated: {}", updatedCourseDto);
			return ResponseEntity.ok(updatedCourseDto);
		} catch (Exception ex) {
			logger.error("An error occurred while editing course: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping()
	public ResponseEntity<Object> getCoursesByCategory(@RequestParam Long categoryId) {
		logger.info("Fetching courses by category ID: {}", categoryId);
		try {
			List<CourseDto> courseDtoList = courseService.getCoursesByCategory(categoryId);
			logger.info("Fetched {} courses by category ID: {}", courseDtoList.size(), categoryId);
			return ResponseEntity.ok(courseDtoList);
		} catch (EntityNotFoundException ex) {
			logger.error("Category not found with ID: {}", categoryId);
			return ResponseEntity.notFound().build();
		}
	}

	private boolean isUserAuthor(Long userId) {
		UserDto author = userService.getUserById(userId);
		return author != null && author.getRole() == UserRole.AUTHOR;
	}
}
