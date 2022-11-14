package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.*;
import com.shopper.walnut.walnut.model.entity.*;
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
            throw new UserPayNotAllow();
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
        orderRepository.delete(optionalOrder.get());
        return "redirect:/user/orderList";
    }

    /** 브랜드 자체 주문 취소 **/
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/brand/main/orderList";
    }
    /** 주소 변경 폼 **/
    @GetMapping("/editAddress")
    public String editAddressForm(@RequestParam Long orderId, Model model){
        Order order =  orderRepository.findById(orderId).get();

        model.addAttribute("order", order);

        return "/order/editAddress";
    }
    /** 배송지 변경**/
    @PostMapping("/editAddress.do")
    public String editAddress(@RequestParam Long orderId, Address address){
        Order order = orderRepository.findById(orderId).get();
        order.getDelivery().setAddress(address);
        orderRepository.save(order);

        return "redirect:/orderList";
    }



}
