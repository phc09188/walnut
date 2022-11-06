package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.Order;
import com.shopper.walnut.walnut.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserAndStatusAndBrand(User user, String status, Brand brand);
    List<Order> findAllByUserAndBrand(User user,Brand brand);
    List<Order> findAllByStatusAndBrand(String status,Brand brand);
    List<Order> findAllByBrand(Brand brand);
}
