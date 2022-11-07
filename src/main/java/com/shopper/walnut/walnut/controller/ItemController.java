package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.ItemException;
import com.shopper.walnut.walnut.exception.PayException;
import com.shopper.walnut.walnut.exception.UserException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.Cart;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.CartRepository;
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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemRepository itemRepository;

    /** 물품 정보**/
    @GetMapping("/info")
    public String itemInfo(Model model, @RequestParam Long itemId){
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty()){
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND);
        }
        String cartOrBuy = "";
        model.addAttribute("item", optionalItem.get());
        model.addAttribute("cartOrBuy", cartOrBuy);
        return "/item/info";
    }




}
