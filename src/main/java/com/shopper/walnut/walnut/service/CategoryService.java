package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.error.CategoryNotExist;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> list() {
        return categoryRepository.findAll();
    }

    /**
     * 서브카테고리로 카테고리 찾기
     **/
    public Category findCategoryName(String id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotExist();
        }
        return optionalCategory.get();
    }

    /**
     * category list 설정
     **/
    public List<String> getCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("상의");
        categoryNames.add("하의");
        categoryNames.add("아우터");
        categoryNames.add("신발");
        return categoryNames;
    }

}
