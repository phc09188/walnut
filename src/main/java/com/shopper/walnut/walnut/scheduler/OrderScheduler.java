package com.shopper.walnut.walnut.scheduler;


import com.shopper.walnut.walnut.model.entity.Order;
import com.shopper.walnut.walnut.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class OrderScheduler {
    private final OrderRepository orderRepository;

    /**
     * 매일 자정
     * 배송 완료가 7일이 지난 주문 정보들은 삭제한다.
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void oldOrderHistoryDelete(){
        List<Order> orders = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for(Order x : orders){
            LocalDateTime dt =  x.getDelivery().getDeliverySuccessDt();
            if(dt.plusDays(7).isAfter(now)){
                long orderId = x.getOrderId();
                orderRepository.delete(x);
                log.info(orderId + "주문 히스토리 삭제");
            }
        }
    }
}
