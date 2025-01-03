package com.vaultis.vaultis_user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String mainPage() {
        return "login";
    }

    @GetMapping("/login/google")
    public String login() {
        return "redirect:/oauth2/authorization/google";
    }

}
