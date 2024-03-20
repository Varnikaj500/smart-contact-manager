package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
//	Database se user lana hai or user detail ko return kar dena hai
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
	User user =	userRepository.getUserByUserName(username);
		
	if(user==null)
	{
		throw new UsernameNotFoundException("Could not found user");
	}
	
	CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails;
	}
	

}
