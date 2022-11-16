package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByFileName(String fileName);

    boolean existsByBrandIdAndBrandItemId(long brandId, long brandItemId);
    boolean existsByBrandItemId(long brandItemId);
    Optional<Item> findByBrandItemId(long brandItemId);
    List<Item> findAllByCategoryName(String categoryName);
    List<Item> findAllBySubCategoryName(String subCategoryName);
}
