package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findAllByCategoryName(String categoryName);

}
