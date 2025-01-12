package com.vaultis.vaultis_user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vaultis.vaultis_user_service.dto.KeyPackage;
import com.vaultis.vaultis_user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;

    @GetMapping("/")
    public String mainPage(Model model) {
        return "myPage";
    }
    
    @GetMapping("/apikey")
    public String apiKey(Model model) {
    	KeyPackage keyPackage = userService.getKeyPackage();
    	model.addAttribute("keyPackage", keyPackage);
    	return "myPage";
    }
    
}
