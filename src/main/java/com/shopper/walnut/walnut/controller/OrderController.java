package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.OrderException;
import com.shopper.walnut.walnut.exception.PayException;
import com.shopper.walnut.walnut.exception.UserException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.model.input.OrderInput;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.CartRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import com.shopper.walnut.walnut.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    /**결제 정보**/
    @GetMapping("/payment")
    public String itemPayment(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                              Model model, @RequestParam String buy,
                              @RequestParam Long itemId, @RequestParam Long itemCnt){

        Optional<User> optionalUser =  userRepository.findById(logInUser.getUsername());
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        User user = optionalUser.get();  Item item = optionalItem.get();
        if(!user.isPayYn()){
            throw new UserException(ErrorCode.USER_PAY_NOT_ALLOW);
        }
        String userId = logInUser.getUsername();
        if(buy.equals("true")) {
            model.addAttribute("itemCnt", itemCnt);
            model.addAttribute("item", item);
            model.addAttribute("user", optionalUser.get());
            return "/order/payment";
        }else{
            Cart cart = new Cart(user, item);
            cart.setCnt(itemCnt);
            cartRepository.save(cart);
            return "redirect:/";
        }
    }
    /** 결제 **/
    @PostMapping("/pay")
    public String itemPay(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                          @RequestParam Long itemId, @RequestParam Long userPoint, @RequestParam Long itemCnt){
        Optional<User> optionalUser = userRepository.findById(logInUser.getUsername());
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        User user = optionalUser.get(); Item item = optionalItem.get();
        long payAmount = (item.getSalePrice() * itemCnt) - userPoint;
        if(user.getUserCache() < payAmount){
            throw new PayException(ErrorCode.NOT_ENOUGH_CACHE);
        }
        if(user.getUserPoint() < userPoint){
            throw new PayException(ErrorCode.POINT_NOT_ENOUGH);
        }

        if(item.getCnt() <=0 || !item.getSaleStatus().equals(ItemStatus.ITEM_STATUS_ING)){
            throw new PayException(ErrorCode.ITEM_IS_EMPTY);
        }
        try {
            orderService.order(user.getUserId(), itemId, itemCnt, userPoint, payAmount);
        } catch (Exception e) {
            return "redirect:/common/error";
        }
        return "redirect:/user/orderList";
    }


    /** 주문 삭제**/
    @PostMapping("/delete.do")
    public String deleteOrder(Long orderId){
        Optional<Order> optionalOrder =  orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new OrderException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.delete(optionalOrder.get());
        return "redirect:/user/orderList";
    }



}
