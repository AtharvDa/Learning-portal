package com.effigo.learningportalv2.dto;

import com.effigo.learningportalv2.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {

	private Long id;
	private String username;
	private String email;
	private UserRole role;
//	@JsonFormat(pattern = "MM/dd/yyyy HH:mm")
//	@CreatedDate
//	private LocalDateTime createdOn;
//
//	@JsonFormat(pattern = "MM/dd/yyyy HH:mm")
//	@LastModifiedDate
//	private LocalDateTime updatedOn;
}
