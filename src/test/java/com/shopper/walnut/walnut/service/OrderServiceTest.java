package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.Order;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.model.input.OrderInput;
import com.shopper.walnut.walnut.model.status.OrderStatus;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.OrderRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void order (){
        //given
        String userId = "1111"; long itemId = 1; long point = 100;
        long count = 10; long payAmount = 10000;
        //when
        long orderId =  orderService.order(userId,itemId,count, point,payAmount);
        //then
        Optional<Order> optional = orderRepository.findById(orderId);
        assertNotNull(optional);
    }

    @Test
    @Transactional
    void cancel(){
        //given
        String userId = "1111"; long itemId = 1; long point = 100;
        long count = 10; long payAmount = 10000;
        long orderId =  orderService.order(userId,itemId,count, point,payAmount);
        Order order = orderRepository.findById(orderId).get();
        //when
        orderService.cancelOrder(order);
        //then
        Optional<Order> optional = orderRepository.findById(orderId);
        boolean result= true;
        if(optional.isEmpty()) result=false;
        assertFalse(result);
    }
    @Test
    @Transactional
    void findAllByString(){
        //given
        OrderInput input = new OrderInput();
        input.setUserName("박해찬"); input.setOrderStatus(OrderStatus.ORDER);
        Brand brand = brandRepository.findById(2L).get();
        //when
        List<Order> list = orderService.findAllByString(input,brand);
        //then
        User user = userRepository.findByUserName(input.getUserName()).get();
        assertEquals(list.size(), orderRepository.findAllByUserAndStatusAndBrand(user, input.getOrderStatus(),brand).size());
    }
}