package com.poppulo.lottery.service;

import com.poppulo.lottery.model.dto.UserDTO;

public interface UserService {

	UserDTO loginUser(UserDTO user) throws Exception;

	UserDTO saveUser(UserDTO user) throws Exception;

	UserDTO getUserByName(UserDTO user) throws Exception;
}
