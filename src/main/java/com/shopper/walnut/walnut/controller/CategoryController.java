package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;


    /**
     * 카테고리에 맞는 상품 view
     **/
    @Cacheable(key = "#model.asMap()", value = CacheKey.CATEGORY_PAGE)
    @GetMapping("/category/categoryList")
    public String categoryItem(@RequestParam String categoryName, Model model) {
        List<Category> subCategoryList = categoryRepository.findAllByCategoryName(categoryName);
        List<Item> items = itemRepository.findAllByCategoryName(categoryName);
        model.addAttribute("subCategoryList", subCategoryList);
        model.addAttribute("items", items);

        return "/category/categoryList";
    }


    /**
     * 서브카테고리에 맞는 상품 view
     **/
    @Cacheable(key = "#model.asMap()", value = CacheKey.SUB_CATEGORY_PAGE)
    @GetMapping("/category/subCategoryList")
    public String subCategoryItem(@RequestParam String subCategoryName, Model model) {
        List<Item> items = itemRepository.findAllBySubCategoryName(subCategoryName);
        model.addAttribute("items", items);
        return "/category/subCategoryList";
    }

}
