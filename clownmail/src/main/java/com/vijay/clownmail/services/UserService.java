package com.vijay.clownmail.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vijay.clownmail.models.Users;
import com.vijay.clownmail.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	    public boolean existsByEmail(String email) {
	        return userRepository.findByEmail(email).isPresent();
	    }

	    public Users registerUser(Users user) {
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        return userRepository.save(user);
	    }

}
