package com.vaultis.vaultis_user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.vaultis.vaultis_user_service.service.CustomLogoutSuccessHandler;
import com.vaultis.vaultis_user_service.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)  
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) 
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/resources/**").permitAll()
						.requestMatchers("/", "/oauth2/**", "/h2-console/**").permitAll()
						.anyRequest().authenticated()  
				)
				// handler 만들어서 로그아웃시 OAuth2User에 담긴걸 비워야한다. 
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessHandler(customLogoutSuccessHandler)
						.logoutSuccessUrl("/")
						.permitAll()
				)  
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(endPoint -> endPoint.userService(customOAuth2UserService)) 
						.defaultSuccessUrl("/", true) 
						.permitAll()
				);

		return http.build();
	}
	
	

}
