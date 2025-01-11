package com.vaultis.vaultis_user_service.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.vaultis.vaultis_user_service.dto.GoogleUserInfo;

@ControllerAdvice
public class UserControllerAdvice {

	
    @ModelAttribute("principal")
    public GoogleUserInfo addPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof GoogleUserInfo) {
            return (GoogleUserInfo) authentication.getPrincipal();
        }
        return null;
    }
    
}
