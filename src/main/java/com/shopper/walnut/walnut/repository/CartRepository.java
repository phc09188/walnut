package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Cart;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserAndItem(User user, Item item);
    List<Cart> findAllByUser(User user);
}
