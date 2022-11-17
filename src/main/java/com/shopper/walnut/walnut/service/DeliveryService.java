package com.shopper.walnut.walnut.service;


import com.shopper.walnut.walnut.model.status.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    /**
     * 배송 상태 확인
     **/
    public DeliveryStatus findDeliveryStatus(String status) {
        if (status.equals(DeliveryStatus.READY.getKey())) {
            return DeliveryStatus.READY;
        } else if (status.equals(DeliveryStatus.COMPLETE.getKey())) {
            return DeliveryStatus.COMPLETE;
        } else {
            return DeliveryStatus.CANCEL;
        }
    }


}

