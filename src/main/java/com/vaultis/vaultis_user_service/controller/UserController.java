package com.vaultis.vaultis_user_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal OAuth2User principal, Model model) {
    	model.addAttribute("principal", principal);
        return "myPage";
    }

    @GetMapping("/login/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }
    
    @GetMapping("/logout")
    public String logout() {
    	System.out.println(">>>>>>>>>>>>>>");
    	return "redirect:/";
    }

}
