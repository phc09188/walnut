package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.input.BrandInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class BrandSignUpServiceTest {
    @Autowired
    private BrandSignUpService service;
    @Test
    @Transactional
    void brandRegister(){
        //given
        BrandInput input = BrandInput
                .builder()
                .brandName("무신사짭2")
                .brandPhone("01011111111")
                .brandLoginId("3333")
                .brandPassword("3333")
                .zipCode("a")
                .streetAdr("a")
                .detailAdr("a")
                .brandRegDt(LocalDateTime.now())
                .build();
        //when
        service.register(input);
        //then
    }

}