package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandItemRepository extends JpaRepository<BrandItem, Integer> {
}
