package com.shopper.walnut.walnut.controller;


import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.input.CategoryName;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.service.CategoryService;
import com.shopper.walnut.walnut.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final CategoryService categoryService;
    private final ItemService itemService;

    //메인
    @RequestMapping("/")
    public String index(Model model, @PageableDefault(page=0, size=10, sort="reviewScore",
    direction = Sort.Direction.DESC)Pageable pageable){
        Page<Item> items = itemService.getAllList(pageable);
        int nowPage = items.getPageable().getPageNumber()+1 ;
        int totalNum = items.getTotalPages();
        int startPage = totalNum < 5 ? 1 : totalNum - 4;
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("startPage", startPage);
        model.addAttribute("items", items);
        model.addAttribute("categoryNames",categoryService.getCategoryNames());
        return "/index";
    }







}
