package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @Test
    void findCategoryNameWithSubCategory(){
        //given
        String subCategoryName = "니트";
        //when
        Category category= categoryService.findCategoryName(subCategoryName);
        //then
        assertEquals(category.getSubCategoryName(), subCategoryName);
    }

    @Test
    void list (){
        //when
        List<Category> list = categoryService.list();
        //then
        assertEquals(list.size(),16);
    }

    @Test
    void getCategoryNames(){
        //when
        List<String> list =  categoryService.getCategoryNames();
        //then
        assertEquals(4, list.size());
    }

}