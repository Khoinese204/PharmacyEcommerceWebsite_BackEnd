package com.example.pharmacywebsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

//disable security
// @SpringBootApplication(exclude = {
// 	org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
// 	org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
// })

@SpringBootApplication
public class PharmacywebsiteApplication {

	@Autowired
	private org.springframework.core.env.Environment env;

	public static void main(String[] args) {
		SpringApplication.run(PharmacywebsiteApplication.class, args);
	}

	// Print biến môi trường để kiểm tra
	@PostConstruct
	public void testEnv() {
		System.out.println("GEMINI_API_KEY (Spring) = " + env.getProperty("GEMINI_API_KEY"));
	}

}
