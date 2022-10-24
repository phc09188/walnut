package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.service.UserSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserSignUpService signUpService;

    @RequestMapping("/member/login")
    public String login(){
        return "member/login";
    }

    @GetMapping("/member/register")
    public String register() {

        return "member/register";
    }

    @PostMapping("/member/register")
    public String registerSubmit(Model model, HttpServletRequest request
            , UserInput parameter) {

        boolean result = signUpService.register(parameter);
        model.addAttribute("result", result);

        return "member/register_complete";
    }
    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = signUpService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "member/email_auth";
    }
}
