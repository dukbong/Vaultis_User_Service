package com.vaultis.vaultis_user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.vaultis.vaultis_user_service.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.headers(header -> header.frameOptions(frameOption -> frameOption.disable()))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/", "/login/google").permitAll()
					.anyRequest().authenticated()
			)
			.logout(logout -> logout.logoutSuccessUrl("/"))
			.oauth2Login(oauth2 -> oauth2
					.loginPage("/login/google")
					.userInfoEndpoint(endPoint -> endPoint.userService(customOAuth2UserService))
					.defaultSuccessUrl("/", true))
			.logout(logout -> logout.logoutSuccessUrl("/"))
			;
		
		return http.build();
	}

}
