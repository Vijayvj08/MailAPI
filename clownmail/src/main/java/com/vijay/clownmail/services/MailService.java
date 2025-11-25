package com.vijay.clownmail.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.clownmail.models.Mail;
import com.vijay.clownmail.repository.MailRepository;

@Service
public class MailService {
	
	@Autowired
	private MailRepository mailRepository;
	//send mail
	public boolean sendMail(Mail mail, String fromEmail) {
        try {
            // logic to send mail or save in DB
        	mail.setFromEmail(fromEmail);
        	mailRepository.save(mail);
//            System.out.println("Mail sent from " + fromEmail + " to " + mail.getToEmail());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	
	//User inbox
	public List<Mail> getInbox(String toemail) {
	    return mailRepository.findByToEmail(toemail);
	}

	
	//view sent mails
	 public List<Mail> getSentMails(String email) {
	        return mailRepository.findByFromEmail(email);
	    }
	 
	 //delete mail by id
	 public void deleteMail(Long id) {
		 mailRepository.deleteById(id);
	 }
}
