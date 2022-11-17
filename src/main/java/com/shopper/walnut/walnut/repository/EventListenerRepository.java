package com.shopper.walnut.walnut.repository;


import com.shopper.walnut.walnut.model.entity.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventListenerRepository extends JpaRepository<EventListener, Long> {
}
