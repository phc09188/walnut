package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.model.input.OrderInput;
import com.shopper.walnut.walnut.model.status.OrderStatus;
import com.shopper.walnut.walnut.repository.CartRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    /**주문 + 장바구니 편집**/
    public Long order(String userId, Long itemId, Long count, Long point ,Long payAmount) {

        User member = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();
        member.setUserPoint(member.getUserPoint() - point);
        member.setUserCache(member.getUserCache() - payAmount);
        item.setTotalTake(payAmount);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        userRepository.save(member);
        Optional<Cart> optionalCart = cartRepository.findByUserAndItem(member,item);
        if(optionalCart.isPresent()){
            cartRepository.delete(optionalCart.get());
        }
        return order.getOrderId();
    }
    /**주문취소**/
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.cancel();
    }

    /** OrderInput에 status와 이름을 입력받아 리스트 반환**/
    public List<Order> findAllByString(OrderInput orderSearch, Brand brand) {
        if(orderSearch.getUserName() == null && orderSearch.getOrderStatus() == null){
            return orderRepository.findAllByBrand(brand);
        }else if(orderSearch.getOrderStatus().equals("")&& orderSearch.getUserName() != null){
            User user = userRepository.findByUserName(orderSearch.getUserName()).get();
            return orderRepository.findAllByUserAndBrand(user,brand);
        }else if(orderSearch.getUserName().equals("") && orderSearch.getOrderStatus() != null){
            String key = orderSearch.getOrderStatus().getKey(); String value = orderSearch.getOrderStatus().getValue();
            return orderRepository.findAllByStatusAndBrand(orderSearch.getOrderStatus(), brand);
        }else{
            User user = userRepository.findByUserName(orderSearch.getUserName()).get();
            return orderRepository.findAllByUserAndStatusAndBrand(user, orderSearch.getOrderStatus(), brand);
        }
    }
}
