package com.vaultis.vaultis_user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
				.csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))  // 헤더 설정
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login/google").permitAll()  // 메인 페이지와 구글 로그인 링크는 허용
						.anyRequest().authenticated()  // 그 외 경로는 인증 필요
				)
				.logout(logout -> logout.logoutSuccessUrl("/"))  // 로그아웃 후 리다이렉트
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(endPoint -> endPoint.userService(customOAuth2UserService))  // 커스텀 OAuth2 서비스
						.defaultSuccessUrl("/", true)  // 로그인 성공 후 메인 페이지로 리다이렉트
				);
		return http.build();
	}

}
