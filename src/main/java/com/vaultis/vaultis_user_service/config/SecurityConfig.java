package com.vaultis.vaultis_user_service.config;

import java.io.IOException;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vaultis.vaultis_user_service.filter.CustomTokenAuthenticationFilter;
import com.vaultis.vaultis_user_service.service.CustomLogoutSuccessHandler;
import com.vaultis.vaultis_user_service.service.CustomOAuth2UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
	private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	
	private final CustomTokenAuthenticationFilter customTokenAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/").permitAll()
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(customTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.deleteCookies("google_webgram")
						.logoutSuccessHandler(customLogoutSuccessHandler)
						.permitAll()
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
