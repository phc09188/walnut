package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.input.BrandStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BrandSignUpServiceTest {
    @Autowired
    private BrandSignUpService service;
    @Test
    void brandRegister(){
        //given
        BrandInput input = BrandInput
                .builder()
                .brandName("무신사짭")
                .brandPhone("01011111111")
                .brandRegDt(LocalDateTime.now())
                .brandStatus(BrandStatus.MEMBER_STATUS_REQ)
                .build();
        //when
        boolean result = service.register(input);
        //then
        assertTrue(result);
    }

}