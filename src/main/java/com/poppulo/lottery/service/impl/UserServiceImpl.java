package com.poppulo.lottery.service.impl;

import com.poppulo.lottery.helper.EntityConversion;
import com.poppulo.lottery.model.User;
import com.poppulo.lottery.model.dto.UserDTO;
import com.poppulo.lottery.repository.UserRepository;
import com.poppulo.lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDTO loginUser(UserDTO userDTO) throws Exception {
		// TODO Auto-generated method stub
		String userpassword = userDTO.getUserPassword();
			User user = userRepository.findByUserName(userDTO.getUserName());
		UserDTO dto = null;
		if (user != null) {
			dto = new UserDTO();
			dto = (UserDTO) EntityConversion.convertModel(user, dto,
					EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());

			if (!bcryptEncoder.matches(userpassword, dto.getUserPassword())) {
				throw new Exception("User Credentials are not valid");
			}
		}

		return dto;
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) throws Exception {
		// TODO Auto-generated method stub
		User user = new User();
		try {
			user = (User) EntityConversion.convertModel(user, userDTO,
					EntityConversion.ConversionEnum.DTOTOENTITY.ordinal());
			user.setUserPassword(bcryptEncoder.encode(user.getUserPassword()));

			user = userRepository.save(user);

			userDTO = (UserDTO) EntityConversion.convertModel(user, userDTO,
					EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return userDTO;
	}

	@Override
	public UserDTO getUserByName(UserDTO userDTO) throws Exception {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(userDTO.getUserName());
		UserDTO dto = null;
		if (user != null) {
			dto = new UserDTO();
			dto = (UserDTO) EntityConversion.convertModel(user, dto,
					EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());

			if (!userDTO.getUserPassword().equals(dto.getUserPassword())) {
				throw new Exception("User Credentials are not valid");
			}
		}

		return dto;
	}
}
