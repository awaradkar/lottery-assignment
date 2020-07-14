package com.poppulo.lottery.controller;

import com.poppulo.lottery.model.dto.UserDTO;
import com.poppulo.lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) throws Exception {
		userDTO = userService.saveUser(userDTO);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}
}
