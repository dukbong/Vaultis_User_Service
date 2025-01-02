package com.vaultis.vaultis_user_service.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.vaultis.vaultis_user_service.entity.User;
import com.vaultis.vaultis_user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
		
		String email = oauth2User.getAttribute("email");
		if(!StringUtils.hasText(email)) {
			throw new OAuth2AuthenticationException("Email is missing in OAuth2User");
		}
		
		saveUser(email);
		
		return oauth2User;
	}
	
	private User saveUser(String email) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			return userRepository.save(new User(email));
		});
	}

}
