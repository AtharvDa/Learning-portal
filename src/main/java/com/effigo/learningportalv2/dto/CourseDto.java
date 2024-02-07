package com.effigo.learningportalv2.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CourseDto {

	private String courseId;
	private String title;
	private CategoryDto category;
	private double price;
	private UserDto author;

	@JsonFormat(pattern = "MM/dd/yyyy HH:mm")
	@CreatedDate
	private LocalDateTime createdOn;

	@JsonFormat(pattern = "MM/dd/yyyy HH:mm")
	@LastModifiedDate
	private LocalDateTime updatedOn;
	// Add other fields as needed

}
