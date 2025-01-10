package com.vaultis.vaultis_user_service.service;

import com.vaultis.vaultis_user_service.dto.GoogleUserInfo;
import com.vaultis.vaultis_user_service.dto.KeyPackage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    public GoogleUserInfo getEmail(String authorizationToken) {
    	if(!StringUtils.hasText(authorizationToken)) {
    		return null;
    	}
    	
        String token = authorizationToken.substring(7);
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token;
        try {
        	GoogleUserInfo googleUserInfo = restTemplate.getForObject(url, GoogleUserInfo.class);
        	if(googleUserInfo != null) {
        		return googleUserInfo;
        	} 
        	return null;
        } catch (Exception e) {
        	return null;
        }
    }

	public KeyPackage getKeyPackage() {
		KeyPackage keyPackage = null;
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048); // RSA 키 길이
	        KeyPair keyPair = keyGen.generateKeyPair();
	        PublicKey publicKey = keyPair.getPublic();
	        PrivateKey privateKey = keyPair.getPrivate();
	        keyPackage = new KeyPackage(Base64.getEncoder().encodeToString(publicKey.getEncoded()),
	        		Base64.getEncoder().encodeToString(privateKey.getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keyPackage;
	}

}
