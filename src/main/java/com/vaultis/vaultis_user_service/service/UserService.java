package com.vaultis.vaultis_user_service.service;

import com.vaultis.vaultis_user_service.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    public String getEmail(String BearerToken) {
        String token = BearerToken.substring(7);
        String url = "https://www.googleapis.com/oauth2/v3?access_token=" + token;
        GoogleUserInfo googleUserInfo = restTemplate.getForObject(url, GoogleUserInfo.class);
        if(googleUserInfo != null) {
            return googleUserInfo.getEmail();
        }
        return null;
    }

}
