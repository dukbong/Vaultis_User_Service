package com.vaultis.vaultis_user_service.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logoutHandler.logout(request, response, authentication);
		
		if(request.getSession() != null) {
			request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
		}
		
		response.sendRedirect("http://localhost:8000/user-service/");
	}

}
