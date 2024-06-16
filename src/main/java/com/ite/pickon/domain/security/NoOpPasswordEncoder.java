package com.ite.pickon.domain.security;

import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log
public class NoOpPasswordEncoder implements PasswordEncoder{
	
	public String encode(CharSequence rawPassword) {
		log.info("before encode : " + rawPassword);
		return rawPassword.toString();
	}
	
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		log.info("matches : " + rawPassword + ":" + encodedPassword);
		return rawPassword.toString().equals(encodedPassword);
	}
}
