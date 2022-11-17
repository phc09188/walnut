package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.UserNotFound;
import com.shopper.walnut.walnut.model.entity.Cart;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.repository.CartRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import com.shopper.walnut.walnut.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    @GetMapping
    public String cartList(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                           Model model) {
        User user = userRepository.findById(logInUser.getUsername())
                .orElseThrow(UserNotFound::new);
        List<Cart> carts = cartRepository.findAllByUser(user);

        model.addAttribute("carts", carts);

        return "/user/cart";
    }

    @PostMapping("/delete.do")
    public String cartDelete(String idList) {
        cartService.del(idList);
        return "redirect:/user/cart";
    }


}
