package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.CategoryException;
import com.shopper.walnut.walnut.exception.ItemException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.dto.BrandItemDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Category;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandItemService {
    private final BrandItemRepository brandItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;


    /**
     * 키로 brandItemId를 받고 싶었으나, input에서 아이디를 얻어올 수 없어서
     * unique 속성을 지니고 있는 fileName(저장 url)을 키로 받아온다.
     */
    @Cacheable(key = "#parameter.fileName", value= CacheKey.ITEM_SCHEDULE)
    public void add(BrandItemInput parameter, Brand brand){
        Optional<Category> optional = categoryRepository.findById(parameter.getSubCategoryName());
        if(optional.isEmpty()){
            throw new CategoryException(ErrorCode.CATEGORY_NOT_EXIST);
        }
        BrandItem item = BrandItemInput.of(parameter);

        item.setBrand(brand);
        brandItemRepository.save(item);

    }

    public BrandItemDto getById(long id) {
        return brandItemRepository.findById(id).map(BrandItemDto::of).orElse(null);
    }



}
