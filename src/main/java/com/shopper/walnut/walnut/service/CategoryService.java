package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> list(){
        return categoryRepository.findAll();
    }

}
