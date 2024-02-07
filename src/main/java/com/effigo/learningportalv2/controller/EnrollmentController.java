package com.effigo.learningportalv2.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.effigo.learningportalv2.dto.EnrollmentDto;
import com.effigo.learningportalv2.service.EnrollmentService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

	private final EnrollmentService enrollmentService;

	public EnrollmentController(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<EnrollmentDto>> getUserEnrollments(@PathVariable Long userId) {
		try {
			List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByUserId(userId);
			logger.info("enrollments : " + enrollments);
			return ResponseEntity.ok(enrollments);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}
}
