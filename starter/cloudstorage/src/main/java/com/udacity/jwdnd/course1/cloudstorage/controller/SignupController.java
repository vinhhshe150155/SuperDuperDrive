package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public String signup(@ModelAttribute User user, Model model) {
        String error = null;
        String successMsg = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            error = "Username existed.";
        }
        if (error == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                error = "An error occurred";
            } else {
                successMsg = "Success";
            }
        }
        model.addAttribute("error", error);
        model.addAttribute("successMsg", successMsg);
        if (successMsg != null) {
            return "redirect:/login";
        }
        return "signup";
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }
}