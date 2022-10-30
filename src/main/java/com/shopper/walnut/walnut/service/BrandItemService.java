package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.BrandItemException;
import com.shopper.walnut.walnut.exception.CategoryException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandItemService {
    private final BrandItemRepository brandItemRepository;
    private final CategoryRepository categoryRepository;

    public void add(BrandItemInput parameter, Brand brand){
        Optional<Category> optional = categoryRepository.findById(parameter.getSubCategoryName());
        if(optional.isEmpty()){
            throw new CategoryException(ErrorCode.CATEGORY_NOT_EXIST);
        }
        BrandItem item = BrandItemInput.of(parameter);
        item.setBrand(brand);
        brandItemRepository.save(item);
        return;
    }
}
