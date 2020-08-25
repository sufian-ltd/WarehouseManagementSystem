package com.warehouse.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.warehouse.entity.User;
import com.warehouse.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username);
		CustomUserDetails customUserDetails = null;
		if (user != null) {
			customUserDetails = new CustomUserDetails();
			customUserDetails.setUser(user);
		} else {
			throw new UsernameNotFoundException("User doesn't exist with the username: " + username);
		}
		return customUserDetails;
	}

}
