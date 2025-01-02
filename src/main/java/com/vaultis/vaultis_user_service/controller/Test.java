package com.vaultis.vaultis_user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {

    @GetMapping("/")
    public String home() {
        return "home"; // home.html을 반환
    }

    @GetMapping("/login/google")
    public String login() {
        // "/oauth2/authorization/google"로 리디렉션하여 구글 로그인 페이지로 이동
        return "redirect:/oauth2/authorization/google";
    }
}
