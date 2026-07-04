package com.ems;

import com.ems.service.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final AppUserService appUserService;

    public RegisterController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {

        String result = appUserService.registerUser(username, password, role);

        if ("success".equals(result)) {
            return "redirect:/login.html?registered=true";
        } else {
            return "redirect:/register.html?error=" + result;
        }
    }
}