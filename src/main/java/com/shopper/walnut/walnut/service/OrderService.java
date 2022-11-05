package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    /**주문**/
    public Long order(String userId, Long itemId, Long count) {

        // Entity 조회
        User member = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);

        return order.getOrderId();
    }
    /**주문취소**/
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findById(orderId).get();
        // 주문 취소
        order.cancel();
    }
}
