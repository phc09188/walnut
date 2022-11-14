package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrandServiceTest {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandItemRepository itemRepository;

    @Test
    @Transactional
    void setTest (){
        Brand brand = brandRepository.findById(Long.valueOf(5)).get();
        //given
        BrandInput input = BrandInput.builder()
                .brandId(brand.getBrandId())
                .brandName("무신사짭수정")
                .brandPhone("01000001111")
                .fileName("우끼끼")
                .urlFileName("우까가")
                .build();
        //when
        boolean result = brandService.set(input);
        //then
        assertTrue(result);
    }
    @Test
    void sendEmail(){
        //given
        BrandItem item = itemRepository.findById(Long.valueOf(1)).get();
        //when
        brandService.sendEmail(item);
        //then
        /** 메일 온거 확인**/
    }

}