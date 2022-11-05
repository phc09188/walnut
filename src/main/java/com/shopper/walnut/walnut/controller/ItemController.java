package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.ItemException;
import com.shopper.walnut.walnut.exception.PayException;
import com.shopper.walnut.walnut.exception.UserException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.entity.Order;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import com.shopper.walnut.walnut.service.ItemService;
import com.shopper.walnut.walnut.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final OrderService orderService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    /** 물품 정보**/
    @GetMapping("/info")
    public String itemInfo(Model model, @RequestParam Long itemId){
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty()){
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND);
        }
        model.addAttribute("item", optionalItem.get());
        return "/item/info";
    }
    /**결제 정보**/
    @GetMapping("/payment")
    public String itemPayment(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                              Model model, @RequestParam String type,
                              @RequestParam Long itemId, @RequestParam Long itemCnt){

        Optional<User> optionalUser =  userRepository.findById(logInUser.getUsername());
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        User user = optionalUser.get();  Item item = optionalItem.get();
        if(!user.isPayYn()){
            throw new UserException(ErrorCode.USER_PAY_NOT_ALLOW);
        }
        String userId = logInUser.getUsername();
        try {
            orderService.order(userId, itemId, itemCnt);
        } catch (Exception e) {
            return "redirect:/common/error";
        }
        if(type.equals("buy")) {
            model.addAttribute("item", item);
            model.addAttribute("user", optionalUser.get());
            return "/item/payment";
        }else{
            return "redirect:/";
        }
    }
    /** 결제 **/
    @PostMapping("/pay")
    public String itemPay(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logInUser,
                          Long itemId, Long userPoint, Model model){
        Optional<User> optionalUser = userRepository.findById(logInUser.getUsername());
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        User user = optionalUser.get(); Item item = optionalItem.get();
        long payAmount = item.getSalePrice() - userPoint;
        if(user.getUserCache() < payAmount){
            throw new PayException(ErrorCode.NOT_ENOUGH_CACHE);
        }
        if(user.getUserPoint() < userPoint){
            throw new PayException(ErrorCode.POINT_NOT_ENOUGH);
        }
        if(item.getCnt() <=0 || item.getSaleStatus() != ItemStatus.ITEM_STATUS_ING){
            throw new PayException(ErrorCode.ITEM_IS_EMPTY);
        }
        //user
        user.setUserCache(user.getUserCache() - payAmount);
        user.setUserPoint(user.getUserPoint() - userPoint);
        // 구매목록 추가
        //item
        // 장바구니 리스트 있으면 없애는 작업 추가

        return "/common/paySuccess";
    }

}
