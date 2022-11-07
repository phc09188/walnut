package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    /**item 리스트 페이징처리**/
    public Page<Item> getAllList(Pageable pageable){
        return itemRepository.findAll(
                PageRequest.of(pageable.getPageNumber(), 10, Sort.by("reviewScore")));
    }

}
