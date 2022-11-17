package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.*;
import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import com.shopper.walnut.walnut.service.CartService;
import com.shopper.walnut.walnut.service.OrderService;
import com.shopper.walnut.walnut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final UserService userService;
    /**결제 정보**/
    @GetMapping("/payment")
    public String itemPayment(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                              Model model, @RequestParam String buy,
                              @RequestParam Long itemId, @RequestParam Long itemCnt){

        User user = userRepository.findById(logInUser.getUsername()).orElseThrow(UserNotFound::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);
        if(!user.isPayYn()){
            throw new UserPayNotAllow();
        }
        if(buy.equals("true")) {
            model.addAttribute("itemCnt", itemCnt);
            model.addAttribute("item", item);
            model.addAttribute("user", user);
            return "/order/payment";
        }else{
            cartService.add(user, item, itemCnt);
            return "redirect:/";
        }
    }
    /** 결제 **/
    @PostMapping("/pay")
    public String itemPay(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                          @RequestParam Long itemId, @RequestParam Long userPoint, @RequestParam Long itemCnt){
        User user = userRepository.findById(logInUser.getUsername()).orElseThrow(UserNotFound::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);
        long payAmount = (item.getSalePrice() * itemCnt) - userPoint;
        if(user.getUserCache() < payAmount){
            throw new NotEnoughCache();
        }
        if(user.getUserPoint() < userPoint){
            throw new PointNotEnough();
        }

        if(item.getCnt() <=0 || !item.getSaleStatus().equals(ItemStatus.ITEM_STATUS_ING)){
            throw new ItemIsEmpty();
        }
        try {
            orderService.order(user.getUserId(), itemId, itemCnt, userPoint, payAmount);
            userService.userMemberShipUpdate(user,payAmount);
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
            throw new OrderNotFound();
        }
        Order order = optionalOrder.get();
        userService.userMemberShipUpdate(order.getUser(),order.getTotalPrice());
        orderRepository.delete(optionalOrder.get());
        return "redirect:/user/orderList";
    }

    /** 브랜드 자체 주문 취소 **/
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if(optional.isEmpty()){
            throw new OrderNotFound();
        }
        orderService.cancelOrder(optional.get());
        return "redirect:/brand/main/orderList";
    }
    /** 주소 변경 폼 **/
    @GetMapping("/editAddress")
    public String editAddressForm(@RequestParam Long orderId, Model model){
        Order order =  orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);

        model.addAttribute("order", order);

        return "/order/editAddress";
    }
    /** 배송지 변경**/
    @PostMapping("/editAddress.do")
    public String editAddress(@RequestParam Long orderId, Address address){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        order.getDelivery().setAddress(address);
        orderRepository.save(order);

        return "redirect:/orderList";
    }



}
