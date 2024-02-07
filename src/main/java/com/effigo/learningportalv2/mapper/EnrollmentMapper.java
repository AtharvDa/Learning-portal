package com.effigo.learningportalv2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.effigo.learningportalv2.dto.EnrollmentDto;
import com.effigo.learningportalv2.entity.Enrollment;

@Mapper
public interface EnrollmentMapper {

	EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

	@Mapping(source = "enrollment.user.id", target = "userId")
	@Mapping(source = "enrollment.course.courseId", target = "courseId")
	EnrollmentDto enrollmentToEnrollmentDto(Enrollment enrollment);

	@Mapping(source = "enrollmentDto.userId", target = "user.id")
	@Mapping(source = "enrollmentDto.courseId", target = "course.courseId")
	Enrollment enrollmentDtoToEnrollment(EnrollmentDto enrollmentDto);

}
