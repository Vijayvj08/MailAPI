	package com.vijay.clownmail.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.clownmail.Security.JwtUtil;
import com.vijay.clownmail.models.Mail;
import com.vijay.clownmail.services.MailService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/mails")
public class MailController {
	
	@Autowired
	private MailService mailService;
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@PostMapping("/send")
	public ResponseEntity<?> sendMail(@RequestBody Mail mail) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String userEmail = auth.getName();

	    boolean sent = mailService.sendMail(mail, userEmail);

	    if (sent) {
	        return ResponseEntity.ok(Map.of(
	            "status", "success",
	            "message", "Mail sent successfully",
	            "from", userEmail,
	            "to", mail.getToEmail()
	        ));
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("status", "error", "message", "Failed to send mail"));
	    }
	}

	
	@GetMapping("/inbox")
	public ResponseEntity<List<Mail>> Inbox(@RequestHeader("Authorization") String authHeader){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName(); // current user's email
	    List<Mail> inbox = mailService.getInbox(email);
	    return ResponseEntity.ok(inbox);

	}
	
	 @GetMapping("/search")
	    public List<Mail> searchMails(@RequestParam String toEmail, HttpServletRequest request) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userEmail = auth.getName();
	        
	        return mailService.searchMailsByRecipient(userEmail, toEmail);
	    }
	 
	 @GetMapping("/sent")
		public ResponseEntity<List<Mail>> sentMail(@RequestHeader("Authorization") String authHeader){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String email = auth.getName(); // current user's email
		    List<Mail> sent = mailService.getSentMails(email);
		    return ResponseEntity.ok(sent);
	 }
}
