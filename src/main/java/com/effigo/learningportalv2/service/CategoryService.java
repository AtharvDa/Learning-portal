package com.effigo.learningportalv2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effigo.learningportalv2.dto.CategoryDto;
import com.effigo.learningportalv2.entity.Category;
import com.effigo.learningportalv2.mapper.CategoryMapper;
import com.effigo.learningportalv2.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * fun : create new category
	 * 
	 * @param categoryDto
	 * @return CategoryDto
	 */
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = CategoryMapper.INSTANCE.categoryDtoToCategory(categoryDto);
		categoryRepository.save(category);
		return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
	}

	/**
	 * fun : get particular category by Id
	 * 
	 * @param id
	 * @return categoryDto
	 */

	public CategoryDto getCategoryById(Long id) {
		Category category = categoryRepository.findById(id).orElse(null);
		return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
	}

	/**
	 * fun : get all available categories
	 * 
	 * @return List<CategoryDto>
	 */
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(CategoryMapper.INSTANCE::categoryToCategoryDto).collect(Collectors.toList());
	}

}
