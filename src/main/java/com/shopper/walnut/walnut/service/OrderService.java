package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.exception.error.BrandNotFound;
import com.shopper.walnut.walnut.exception.error.ItemNotFound;
import com.shopper.walnut.walnut.exception.error.OrderNotFound;
import com.shopper.walnut.walnut.exception.error.UserNotFound;
import com.shopper.walnut.walnut.model.entity.*;
import com.shopper.walnut.walnut.model.input.OrderInput;
import com.shopper.walnut.walnut.model.status.DeliveryStatus;
import com.shopper.walnut.walnut.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final BrandRepository brandRepository;

    /**
     * 주문 + 장바구니 편집
     **/
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long order(String userId, Long itemId, Long count, Long point, Long payAmount) {

        User member = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);
        Brand brand = brandRepository.findById(item.getBrandId()).orElseThrow(BrandNotFound::new);
        member.setUserPoint(member.getUserPoint() - point);
        member.setUserCache(member.getUserCache() - payAmount);
        item.setTotalTake(payAmount);
        item.setPayAmount(item.getPayAmount() + count);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, brand, delivery, orderItem);
        orderRepository.save(order);
        userRepository.save(member);
        Optional<Cart> optionalCart = cartRepository.findByUserAndItem(member, item);
        optionalCart.ifPresent(cartRepository::delete);
        return order.getOrderId();
    }

    /**
     * 주문취소
     **/
    public void cancelOrder(Long orderId) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if(optional.isEmpty()){
            throw new OrderNotFound();
        }
        orderRepository.delete(optional.get());
    }

    /**
     * OrderInput에 status와 이름을 입력받아 리스트 반환
     **/
    public List<Order> findAllByString(OrderInput orderSearch, Brand brand) {
        if (orderSearch.getUserName() == null && orderSearch.getOrderStatus() == null) {
            return orderRepository.findAllByBrand(brand);
        } else if (orderSearch.getOrderStatus().equals("") && orderSearch.getUserName() != null) {
            User user = userRepository.findByUserName(orderSearch.getUserName()).get();
            return orderRepository.findAllByUserAndBrand(user, brand);
        } else if (orderSearch.getUserName().equals("") && orderSearch.getOrderStatus() != null) {
            return orderRepository.findAllByStatusAndBrand(orderSearch.getOrderStatus(), brand);
        } else {
            User user = userRepository.findByUserName(orderSearch.getUserName()).orElseThrow(UserNotFound::new);
            return orderRepository.findAllByUserAndStatusAndBrand(user, orderSearch.getOrderStatus(), brand);
        }
    }

}
