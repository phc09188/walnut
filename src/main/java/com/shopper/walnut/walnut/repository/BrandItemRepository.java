package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BrandItemRepository extends JpaRepository<BrandItem, Long> {
    List<BrandItem> findAllByBrand(Brand brand);

    List<BrandItem> findAllByAddDtBetween(LocalDate start, LocalDate end);

}
