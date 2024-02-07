package com.effigo.learningportalv2.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.effigo.learningportalv2.dto.CourseDto;
import com.effigo.learningportalv2.dto.UserDto;
import com.effigo.learningportalv2.enums.UserRole;
import com.effigo.learningportalv2.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api")
/**
 * Contains all User related end-points
 */

public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * fetches all users -- Admin-route
	 * 
	 * @param adminId
	 * 
	 * @return
	 */
	@GetMapping("/admin")
	public ResponseEntity<Object> getAllUsers(@RequestParam Long adminId) {

		if (isUserAdmin(adminId)) {
			logger.info("Request to fetch all users");
			List<UserDto> users = userService.getAllUsers();
			return ResponseEntity.ok(users);
		} else {
			logger.warn("Unauthorized access to create course by user ID: {}", adminId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
		}

	}

	/**
	 * fetch user by userId - Admin - Route
	 * 
	 * @param adminId
	 * @param userId
	 * @return
	 */

	@GetMapping("/admin/{userId}")
	public ResponseEntity<Object> getUserById(@RequestParam Long adminId, @PathVariable Long userId) {
		if (isUserAdmin(adminId)) {
			logger.info("Request to fetch user by ID: {}", userId);
			return ResponseEntity.ok(userService.getUserById(userId));
		} else {
			logger.warn("Unauthorized access to create user by user ID: {}", adminId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
		}

	}

	/**
	 * Create new user - admin route
	 * 
	 * @param adminId
	 * @param userDto
	 * @return
	 */
	@PostMapping("/admin/{adminId}")
	public ResponseEntity<Object> createUser(@PathVariable Long adminId, @RequestBody UserDto userDto) {

		if (isUserAdmin(adminId)) {
			logger.info("Request create a new user");
			UserDto newUserDto = userService.createUser(userDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(newUserDto);
		} else {
			logger.warn("Unauthorized access to create user by user ID: {}", adminId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
		}
	}

	/**
	 * Basic check if user is admin or not
	 * 
	 * @param userId
	 * @return
	 */

	private boolean isUserAdmin(Long userId) {
		UserDto admin = userService.getUserById(userId);
		return admin != null && admin.getRole() == UserRole.ADMIN;
	}

	/**
	 * Enrollment in particular course
	 * 
	 * @param userId
	 * @param courseId
	 * @return
	 */
	@PostMapping("user/enroll")
	public ResponseEntity<Object> enrollCourse(@RequestParam Long userId, @RequestParam String courseId) {
		try {
			userService.enrollCourse(userId, courseId);
			return ResponseEntity.ok("Enrolled in the course successfully.");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * add courses to favorites by user
	 * 
	 * @param userId
	 * @param courseId
	 * @return
	 */

	@PostMapping("user/favorites")
	public ResponseEntity<Object> addToFavorites(@RequestParam Long userId, @RequestParam String courseId) {
		try {
			userService.addToFavorites(userId, courseId);
			return ResponseEntity.ok("Added course to favorites successfully.");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add course to favorites.");
		}
	}

	@GetMapping("/{userId}/favorites")
	public ResponseEntity<Object> getUserFavorites(@PathVariable Long userId) {
		logger.info("Fetching favorite courses for user with ID: {}", userId);
		try {
			List<CourseDto> favorites = userService.getUserFavorites(userId);
			logger.info("Fetched {} favorite courses for user with ID: {}", favorites.size(), userId);
			return ResponseEntity.ok(favorites);
		} catch (EntityNotFoundException ex) {
			logger.error("User not found with ID: {}", userId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		} catch (Exception ex) {
			logger.error("An error occurred while fetching user favorites: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}
}
