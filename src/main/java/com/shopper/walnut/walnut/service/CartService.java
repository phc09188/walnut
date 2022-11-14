package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.model.entity.Cart;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.entity.User;
import com.shopper.walnut.walnut.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    public boolean del(String idList) {
        if (idList != null && idList.length() > 0) {
            String[] ids = idList.split(",");
            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (Exception e) {
                }
                if (id > 0) {
                    cartRepository.deleteById(id);
                }
            }
        }

        return true;
    }

    public void add(User user, Item item, Long itemCnt) {
        Cart cart = new Cart(user, item);
        cart.setCnt(itemCnt);
        cartRepository.save(cart);
    }
}
