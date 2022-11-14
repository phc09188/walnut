package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.ItemNotFound;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
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
            throw new ItemNotFound();
        }
        Item item = optionalItem.get();
        item.setViewCount(item.getViewCount()+1);
        itemRepository.save(item);
        String cartOrBuy = "";
        model.addAttribute("item",item);
        model.addAttribute("cartOrBuy", cartOrBuy);
        return "/item/info";
    }





}
