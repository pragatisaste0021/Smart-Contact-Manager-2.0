package com.scm.scm20;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.scm20.services.EmailService;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	// @Test
	// void sendEmailTest(){
	// 	emailService.sendEmail("pragati.saste21@gmail.com", "Just testing email service", "This is scm project working on email service");
	// }

}
