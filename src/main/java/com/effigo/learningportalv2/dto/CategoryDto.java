package com.effigo.learningportalv2.dto;

import java.util.List;

import com.effigo.learningportalv2.entity.Course;

import lombok.Data;

@Data
public class CategoryDto {
	private Long categoryId;
	private String categoryName;
	List<Course> courses;

}
