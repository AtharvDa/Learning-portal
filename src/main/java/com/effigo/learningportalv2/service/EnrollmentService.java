package com.effigo.learningportalv2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.effigo.learningportalv2.dto.EnrollmentDto;
import com.effigo.learningportalv2.entity.Enrollment;
import com.effigo.learningportalv2.entity.User;
import com.effigo.learningportalv2.mapper.EnrollmentMapper;
import com.effigo.learningportalv2.repository.EnrollmentRepository;
import com.effigo.learningportalv2.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnrollmentService {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

	private final UserRepository userRepository;
	private final EnrollmentRepository enrollmentRepository;

	/**
	 * Constructor Injection
	 * 
	 * @param userRepository
	 * @param enrollmentRepository
	 */
	public EnrollmentService(UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
		this.userRepository = userRepository;
		this.enrollmentRepository = enrollmentRepository;
	}

	/**
	 * fun : getting enrollments of particular user
	 * 
	 * @param userId
	 * @return
	 */
	public List<EnrollmentDto> getEnrollmentsByUserId(Long userId) {
		logger.info("Fetching enrollments for user ID: {}", userId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

		// Retrieving enrollments for the user
		List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

		logger.info("Found {} enrollments for user ID: {}", enrollments.size(), userId);
		logger.info("Found enrollments " + enrollments);

		return enrollments.stream().map(EnrollmentMapper.INSTANCE::enrollmentToEnrollmentDto)
				.collect(Collectors.toList());
	}

}
