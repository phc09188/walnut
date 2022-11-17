package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.error.BrandItemNotExist;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final BrandItemRepository brandItemRepository;

    /**
     * item 리스트 페이징처리
     **/
    public Page<Item> getAllList(Pageable pageable) {
        return itemRepository.findAll(
                PageRequest.of(pageable.getPageNumber(), 10, Sort.by("reviewScore")));
    }


    /**
     * 캐시에 저장된 정보를 가져와서 업데이트 후 삭제
     **/
    @CacheEvict(value = CacheKey.ITEM_SCHEDULE, key = "#brandItemId")
    public void cacheDelete(long brandItemId) {
        boolean update = itemRepository.existsByBrandItemId(brandItemId);
        if (!update) {
            BrandItem item = brandItemRepository.findById(brandItemId).orElseThrow(BrandItemNotExist::new);
            itemRepository.save(Item.of(item));
        }
    }


}
