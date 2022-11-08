package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.model.entity.Order;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.OrderInput;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import com.shopper.walnut.walnut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    /**로그인(Security)**/
    @RequestMapping("/login")
    String login(){
        return "user/login";
    }
    /**회원가입 폼**/
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("memberForm",new UserInput());
        return "user/register";
    }
    /** 회원가입 **/
    @PostMapping("/register/write")
    public String registerSubmit(@Validated UserInput parameter) {
        userService.register(parameter);

        return "redirect:/";
    }
    /**이메일 인증 페이지**/
    @GetMapping("/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {
        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = userService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "user/email_auth";
    }
    /**마이페이지**/
    @GetMapping("/myPage")
    public String myPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                         Model model){
        User user = userRepository.findById(logInUser.getUsername()).get();
        String memberShip =  userService.memberShip(user);

        model.addAttribute("user" ,user);
        model.addAttribute("memberShip", memberShip);
        return "/user/myPage";
    }

    /** 캐시 충전 폼**/
    @GetMapping("/cache")
    public String cacheRefill(Model model,
                              @AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser){
        User user =  userRepository.findById(logInUser.getUsername()).get();
        model.addAttribute("user", user);
        return "user/cacheRefill";
    }
    /** 캐시 충전 **/
    @PostMapping("/cacheUp")
    public String cacheUp(String userId, long cache){
        userService.cacheUp(userId, cache);
        return "redirect:/user/myPage";
    }

    /** 주문 리스트 **/
    @GetMapping("/orderList")
    public String orderList(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                            Model model){
        User user = userRepository.findById(logInUser.getUsername()).get();
        List<Order> orders = orderRepository.findAllByUser(user);

        model.addAttribute("orders", orders);
        return "/user/orderList";
    }

    /** 유저 정보 **/
    @GetMapping("/info")
    public String userInfo(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                           Model model){
        User user = userRepository.findById(logInUser.getUsername()).get();

        model.addAttribute("user", user);
        return "/user/info";
    }
    /** 회원정보 수정 **/
    @PostMapping("/info/update")
    public String userUpdate(UserInput input){
        User user = userRepository.findById(input.getUserId()).get();
        userService.userUpdate(user, input);
        return "redirect:/user/logout";
    }

    /** 회원 탈퇴 **/
    @PostMapping("/withdraw")
    public String userWithdraw(@RequestParam String userId){
        User user = userRepository.findById(userId).get();
        userRepository.delete(user);
        return "redirect:/";
    }


}
