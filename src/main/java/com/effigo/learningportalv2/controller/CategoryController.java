package com.effigo.learningportalv2.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.effigo.learningportalv2.dto.CategoryDto;
import com.effigo.learningportalv2.service.CategoryService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
		logger.info("Fetching category by ID: {}", id);
		CategoryDto categoryDto = categoryService.getCategoryById(id);
		logger.info("Retrieved category: {}", categoryDto);
		return ResponseEntity.ok(categoryDto);
	}

	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		logger.info("Fetching all categories");
		List<CategoryDto> categories = categoryService.getAllCategories();
		logger.info("Retrieved {} categories", categories.size());
		return ResponseEntity.ok(categories);
	}

	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
		logger.info("Creating category: {}", categoryDto);
		CategoryDto createdCategory = categoryService.createCategory(categoryDto);
		logger.info("Created category: {}", createdCategory);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
		logger.error("Entity not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> handleException(Exception ex) {
		logger.error("Internal server error: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
	}
}
