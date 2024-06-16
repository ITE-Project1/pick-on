package com.ite.pickon.domain.security;

import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		log.info("Login Success");
		List<String> roleNames = new ArrayList<>();
		auth.getAuthorities().forEach(authority -> {
			roleNames.add(authority.getAuthority());
		});
		
		log.info("ROLE NAMES: " + roleNames);
		if (roleNames.contains("ROLE_ADMIN")) {
			response.sendRedirect("/");
			return ;
		}
		if (roleNames.contains("ROLE_MEMBER")) {
			response.sendRedirect("/");
			return ;
		}
		response.sendRedirect("/");
		
	}

}
