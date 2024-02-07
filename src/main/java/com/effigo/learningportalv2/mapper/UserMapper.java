package com.effigo.learningportalv2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.effigo.learningportalv2.dto.UserDto;
import com.effigo.learningportalv2.entity.User;

@Mapper
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

//	@Mapping(target = "password", ignore = true)
	UserDto userToUserDto(User user);

	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "enrollments", ignore = true)
	@Mapping(target = "favorites", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	User userDtoToUser(UserDto userDto);
}
