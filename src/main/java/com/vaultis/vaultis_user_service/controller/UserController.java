package com.vaultis.vaultis_user_service.controller;

import com.vaultis.vaultis_user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String mainPage(@RequestHeader Map<String, String> headers,@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
    	model.addAttribute("principal", oauth2User);
        return "myPage";
    }

    @GetMapping("/login/google")
    public String googleLogin() {
        return "redirect:/oauth2/authorization/google";
    }
    
    @GetMapping("/logout")
    public String logout() {
    	return "redirect:/user-service";
    }

}
