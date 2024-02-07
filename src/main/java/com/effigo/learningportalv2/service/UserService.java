package com.effigo.learningportalv2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.effigo.learningportalv2.dto.CourseDto;
import com.effigo.learningportalv2.dto.UserDto;
import com.effigo.learningportalv2.entity.Course;
import com.effigo.learningportalv2.entity.Enrollment;
import com.effigo.learningportalv2.entity.User;
import com.effigo.learningportalv2.mapper.CourseMapper;
import com.effigo.learningportalv2.mapper.UserMapper;
import com.effigo.learningportalv2.repository.CourseRepository;
import com.effigo.learningportalv2.repository.EnrollmentRepository;
import com.effigo.learningportalv2.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final CourseRepository courseRepository;

	/**
	 * Constructor injection
	 * 
	 * @param userRepository
	 * @param enrollmentRepository
	 * @param courseRepository
	 */
	public UserService(UserRepository userRepository, EnrollmentRepository enrollmentRepository,
			CourseRepository courseRepository) {
		this.userRepository = userRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.courseRepository = courseRepository;

	}

	/**
	 * fun : getting all registered user Response - List of all users
	 * 
	 * @return
	 */
	public List<UserDto> getAllUsers() {
		logger.info("Fetching all users");
		List<User> users = userRepository.findAll();
		return users.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
	}

	/**
	 * 
	 * fun : getting user by userId
	 * 
	 * @param userId
	 * @return
	 */
	public UserDto getUserById(Long userId) {
		logger.info("Fetching user by ID: {}", userId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		return UserMapper.INSTANCE.userToUserDto(user);
	}

	/**
	 * fun : create new user Response : Created User
	 * 
	 * @param userDto
	 * @return
	 */
	public UserDto createUser(UserDto userDto) {
		logger.info("Creating new user: {}", userDto.getUsername());

		User user = UserMapper.INSTANCE.userDtoToUser(userDto);
		User savedUser = userRepository.save(user);
		logger.info("User created successfully: {}", savedUser.getUsername());
		return UserMapper.INSTANCE.userToUserDto(savedUser);
	}

	/**
	 * fun : enroll in particular course
	 * 
	 * @param userId
	 * @param courseId
	 */
	public void enrollCourse(Long userId, String courseId) {
		// Retrieving user and course from the database
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

		// Check if the user is already enrolled in the course
		boolean isEnrolled = enrollmentRepository.existsByUserAndCourse(user, course);
		if (isEnrolled) {
			throw new IllegalStateException("User is already enrolled in the course");
		}

		// Enrolling the user in the course
		Enrollment enrollment = new Enrollment();
		enrollment.setUser(user);
		enrollment.setCourse(course);

		enrollmentRepository.save(enrollment);
	}

	/**
	 * fun : add courses to favorites
	 * 
	 * @param userId
	 * @param courseId
	 */
	public void addToFavorites(Long userId, String courseId) {
		// Retrieving user and course from the database
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

		user.getFavorites().add(course);

		// Save the updated user entity
		userRepository.save(user);
	}

	/**
	 * fun : getting user favorite courses
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<CourseDto> getUserFavorites(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
		return user.getFavorites().stream().map(CourseMapper.INSTANCE::courseToCourseDto).collect(Collectors.toList());
	}

}
