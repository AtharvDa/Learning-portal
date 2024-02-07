package com.effigo.learningportalv2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.effigo.learningportalv2.dto.CourseDto;
import com.effigo.learningportalv2.entity.Course;

@Mapper
public interface CourseMapper {

	CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

	CourseDto courseToCourseDto(Course course);

	Course courseDtoToCourse(CourseDto courseDto);
}
