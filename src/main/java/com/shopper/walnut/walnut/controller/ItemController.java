package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.ItemException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping("/info")
    public String itemInfo(Model model, @RequestParam Long itemId){
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty()){
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND);
        }
        model.addAttribute("item", optionalItem.get());
        return "/item/info";
    }
    @GetMapping("/payment")
    public String itemPayment(Model model, @RequestParam Long itemId){
        return "/item/payment";
    }

}
