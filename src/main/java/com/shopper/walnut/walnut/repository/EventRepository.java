package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.Event;
import com.shopper.walnut.walnut.model.status.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByBrand(Brand brand);

    List<Event> findAllByStatus(EventStatus status);

    List<Event> findAllBySignDateGreaterThanEqualAndSignDateLessThanEqual(LocalDate start, LocalDate now);
}
