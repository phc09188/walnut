package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.service.UserSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserSignUpService signUpService;

    @RequestMapping("/user/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/user/register")
    public String register() {

        return "user/register";
    }

    @PostMapping("/user/register")
    public String registerSubmit(Model model, HttpServletRequest request
            , UserInput parameter) {

        boolean result = signUpService.register(parameter);
        model.addAttribute("result", result);

        return "user/register_complete";
    }
    @GetMapping("/user/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = signUpService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "user/email_auth";
    }



}