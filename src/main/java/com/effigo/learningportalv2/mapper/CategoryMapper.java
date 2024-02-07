package com.effigo.learningportalv2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.effigo.learningportalv2.dto.CategoryDto;
import com.effigo.learningportalv2.entity.Category;

@Mapper
public interface CategoryMapper {

	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	Category categoryDtoToCategory(CategoryDto categoryDto);

	CategoryDto categoryToCategoryDto(Category category);

}
