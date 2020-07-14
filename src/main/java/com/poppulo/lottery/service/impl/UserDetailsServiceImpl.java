package com.poppulo.lottery.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.poppulo.lottery.model.User;
import com.poppulo.lottery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username:" + username);
		}
		final UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserName(),
				user.getUserPassword(), getAuthorities(user.getUserRole()));
		return userDetails;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String userRole) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(userRole));
		return authorities;
	}
}
