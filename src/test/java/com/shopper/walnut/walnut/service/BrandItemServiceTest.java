package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.dto.BrandItemDto;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BrandItemServiceTest {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandItemRepository brandItemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandItemService service;

    /**
     * 물품 등록
     */
    @Test
    void addTest(){
        //given
        Brand brand = brandRepository.findById(Long.valueOf(2)).get();
//        long curCnt= brand.getList().size();
        BrandItemInput input = BrandItemInput.builder()
                .categoryName("상의")
                .subCategoryName("니트")
                .itemName("aaa")
                .price(0)
                .fileName("asdf")
                .fileUrl("aefd")
                .salePrice(0)
                .itemInfo("aaa")
                .saleStatus(ItemStatus.ITEM_STATUS_ING)
                .cnt(0)
                .build();
        //when
        service.add(input, brand);
        //then
    }
    /**
     * Id로 아이템 유무 확인
     */
    @Test
    void itemExistTest(){
        //given
        long id = 1;
        //when
        BrandItemDto dto = service.getById(id);
        //then
        assertEquals(dto.getBrandItemId(),id);
    }
    /**
     * 총 매출액 확인
     */
    @Test
    void totalAmount(){
        //given
        List<BrandItem> items = brandItemRepository.findAllByBrand(
                brandRepository.findById(Long.valueOf(2))
                        .get());
        //when
        long amount = service.findTotalAmount(items);
        //then
        /** 개발 단계에 따라 뒤에 19900원은 변할 수 있다.**/
        assertEquals(amount,19900);
    }

}