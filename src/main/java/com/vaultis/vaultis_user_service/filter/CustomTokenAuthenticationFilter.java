package com.vaultis.vaultis_user_service.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vaultis.vaultis_user_service.dto.GoogleUserInfo;
import com.vaultis.vaultis_user_service.template.RestTemplateConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {

	private final RestTemplate restTemplate;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 필터 동작");
		String bearerToken = request.getHeader("authorization");
		if(StringUtils.hasText(bearerToken)) {
			String token = bearerToken.substring(7);
			
			String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token;
	        try {
                GoogleUserInfo googleUserInfo = restTemplate.getForObject(url, GoogleUserInfo.class);
                if (googleUserInfo != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(
                            googleUserInfo, 
                            null, 
                            null
                        );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		
		filterChain.doFilter(request, response);
	}

}
