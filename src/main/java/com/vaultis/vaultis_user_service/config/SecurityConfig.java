package com.vaultis.vaultis_user_service.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.vaultis.vaultis_user_service.service.CustomLogoutSuccessHandler;
import com.vaultis.vaultis_user_service.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
	private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/resources/**", "/").permitAll()
						.anyRequest().authenticated()
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessHandler(customLogoutSuccessHandler)
						.logoutSuccessUrl("/")
//						.permitAll()
				)
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(endPoint -> endPoint.userService(customOAuth2UserService)).successHandler(this::oauth2SuccessHandler)
				);

		return http.build();
	}

	private void oauth2SuccessHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
		OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

		OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(
				oauth2AuthenticationToken.getAuthorizedClientRegistrationId(),
				oauth2AuthenticationToken.getName()
		);

		String tokenValue = authorizedClient.getAccessToken().getTokenValue();

		Cookie cookie = new Cookie(oauth2AuthenticationToken.getAuthorizedClientRegistrationId() + "_webgram", tokenValue);
		cookie.setPath("/");
		httpServletResponse.addCookie(cookie);
		// google : token

		httpServletResponse.sendRedirect("http://localhost:8000/user-service/");
	}

}
