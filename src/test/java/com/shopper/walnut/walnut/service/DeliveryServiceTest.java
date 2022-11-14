package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.status.DeliveryStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryServiceTest {

    @Autowired
    private DeliveryService service;

    @Test
    void findDeliveryStatus(){
        //given
        String status = DeliveryStatus.CANCEL.getKey();
        //when
        DeliveryStatus deliveryStatus =  service.findDeliveryStatus(status);
        //then
        assertEquals(deliveryStatus, DeliveryStatus.READY);
    }
}