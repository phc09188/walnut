package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.error.CategoryNotExist;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.dto.BrandItemDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandItemService {
    private final BrandItemRepository brandItemRepository;
    private final CategoryRepository categoryRepository;


    /**
     * 키로 brandItemId를 받아온다.
     */
    @Cacheable(key = "#parameter.brandItemId", value= CacheKey.ITEM_SCHEDULE)
    public void add(BrandItemInput parameter, Brand brand){
        Optional<Category> optional = categoryRepository.findById(parameter.getSubCategoryName());
        if(optional.isEmpty()){
            throw new CategoryNotExist();
        }
        BrandItem item = BrandItemInput.of(parameter);

        item.setBrand(brand);
        brandItemRepository.save(item);

    }

    public BrandItemDto getById(long id) {
        return brandItemRepository.findById(id).map(BrandItemDto::of).orElse(null);
    }


    public long findTotalAmount(List<BrandItem> items) {
        long amount = 0;
        for(BrandItem x : items){
            amount += x.getTotalTake();
        }
        return amount;
    }
}
