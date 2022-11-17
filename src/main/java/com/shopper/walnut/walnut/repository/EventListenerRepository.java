package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.Event;
import com.shopper.walnut.walnut.model.entity.EventListener;
import com.shopper.walnut.walnut.model.status.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventListenerRepository extends JpaRepository<EventListener, Long> {
}
