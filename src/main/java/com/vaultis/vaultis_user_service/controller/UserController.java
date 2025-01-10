package com.vaultis.vaultis_user_service.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vaultis.vaultis_user_service.dto.GoogleUserInfo;
import com.vaultis.vaultis_user_service.dto.KeyPackage;
import com.vaultis.vaultis_user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;

    @GetMapping("/")
    public String mainPage(Model model) {
    	GoogleUserInfo principal = null;
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            principal = (GoogleUserInfo) authentication.getPrincipal();
        }
        model.addAttribute("principal", principal);
        return "myPage";
    }
    
    @GetMapping("/apikey")
    public String apiKey(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	KeyPackage keyPackage = userService.getKeyPackage();
    	model.addAttribute("principal", (GoogleUserInfo) authentication.getPrincipal());
    	model.addAttribute("keyPackage", keyPackage);
    	return "myPage";
    }
    
}
