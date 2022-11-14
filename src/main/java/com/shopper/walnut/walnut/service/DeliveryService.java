package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Delivery;
import com.shopper.walnut.walnut.model.entity.Order;
import com.shopper.walnut.walnut.model.status.DeliveryStatus;
import com.shopper.walnut.walnut.model.status.OrderStatus;
import com.shopper.walnut.walnut.repository.DeliveryRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    /** 배송 상태 확인 **/
    public DeliveryStatus findDeliveryStatus(String status) {
        if(status.equals(DeliveryStatus.READY.getKey())){
            return DeliveryStatus.READY;
        }else if(status.equals(DeliveryStatus.COMPLETE.getKey())){
            return DeliveryStatus.COMPLETE;
        }else{
            return DeliveryStatus.CANCEL;
        }
    }


}

