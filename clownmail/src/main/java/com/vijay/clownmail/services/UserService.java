package com.vijay.clownmail.services;

import java.util.Optional;

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
	    
	    public boolean resetPassword(String email, String newPassword) {
	    	Optional<Users> userOpt = userRepository.findByEmail(email);
	    	
	    	if(userOpt.isPresent()) {
	    		Users user = userOpt.get();
	    		
	    		user.setPassword(passwordEncoder.encode(newPassword));
	    		userRepository.save(user);
	    		return true;
	    	}
	    	return false;
	    }

}
