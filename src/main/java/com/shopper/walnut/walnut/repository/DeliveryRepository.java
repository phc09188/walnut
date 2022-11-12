package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository  extends JpaRepository<Delivery, Long> {

}
