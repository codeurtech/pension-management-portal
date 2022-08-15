package com.pension.management.authorizationservice.service;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.pension.management.authorizationservice.exception.InvalidCredentialsException;
import com.pension.management.authorizationservice.model.User;
import com.pension.management.authorizationservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {
	
	private static Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	public Optional<User> findByUsername(String username) {
		return userRepository.findById(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> userOptional = findByUsername(username);
		if (!userOptional.isPresent()) {
			throw new InvalidCredentialsException("USER NOT PRESENT");
		} else {
			log.info("Valid Username: {}", username);
			User user = userOptional.get();
			return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
		}
	}

}
