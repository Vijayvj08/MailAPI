package com.vijay.clownmail.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.clownmail.Security.JwtUtil;
import com.vijay.clownmail.models.Users;
import com.vijay.clownmail.services.CustomUserService;
import com.vijay.clownmail.services.UserService;

@RestController
@RequestMapping("api/users")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomUserService customUserService;
	@Autowired
	private UserService userService;
	
	 @PostMapping("/register")
	    public ResponseEntity<?> registerUser(@RequestBody Users user) {
	        if (userService.existsByEmail(user.getEmail())) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body(Map.of("status", "error", "message", "Email already exists"));
	        }

	        Users savedUser = userService.registerUser(user);

	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(Map.of(
	                        "status", "success",
	                        "message", "User registered successfully",
	                        "userId", savedUser.getId(),
	                        "email", savedUser.getEmail()));
	    }
	
		@PostMapping("/login")
		public ResponseEntity<?> login(@RequestBody Users user) {
			
			if (!userService.existsByEmail(user.getEmail())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("message", "User not found!"));
			}

			try {
				authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			} catch (BadCredentialsException e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("message", "Incorrect Password! Try again."));
			}

			final UserDetails userDetails = customUserService.loadUserByUsername(user.getEmail());
			final String jwt = jwtUtil.generateToken(userDetails.getUsername());

			return ResponseEntity.ok(Map.of("token", jwt));
		}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request){
		String email = request.get("email");
		String newPassword = request.get("password");
		
		boolean isReset = userService.resetPassword(email, newPassword);
		
		if(isReset) {
			return ResponseEntity.ok(Map.of("message", "password rest sucessfully"));
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Email not found"));
		}
	}
}
