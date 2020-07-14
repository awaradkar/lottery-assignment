package com.poppulo.lottery.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.poppulo.lottery.model.dto.UserDTO;
import com.poppulo.lottery.security.JwtTokenUtil;
import com.poppulo.lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/login")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO dto) throws Exception {
		authenticate(dto.getUserName(), dto.getUserPassword());

		dto = userService.loginUser(dto);

		final UserDetails userDetails = new User(dto.getUserName(), dto.getUserPassword(),
				getAuthorities(dto.getUserRole()));
		final String token = jwtTokenUtil.generateToken(userDetails);
		dto.setUserPassword("");
		return ResponseEntity.ok(dto);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String userRole) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(userRole));
		return authorities;
	}
}
