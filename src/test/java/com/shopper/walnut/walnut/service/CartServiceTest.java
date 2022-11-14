package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Cart;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.repository.CartRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Transactional
    void add(){
        //given
        User user = userRepository.findById("1111").get();
        Item item = itemRepository.findById(Long.valueOf(1)).get();
        long itemCnt = 10;
        //when
        cartService.add(user, item, itemCnt);

        //then
        Optional<Cart> optionalCart = cartRepository.findByUserAndItem(user, item);
        assertNotNull(optionalCart);
    }

    @Test
    @Transactional
    void del(){
        //given
        String idList = "1,2,3";
        //when
        boolean result = cartService.del(idList);
        //then
        assertTrue(result);
    }

}