package com.poppulo.lottery.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO {

	private UUID userId;

	private String userName;

	private String userPassword;

	private String userRole;

	private String token;

	@Override
	public boolean equals(Object obj) {
		UserDTO dto = (UserDTO)obj;
		return this.getUserName().equals(dto.getUserName());
	}
}
