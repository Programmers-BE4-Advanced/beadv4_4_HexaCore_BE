package com.back.product.adapter.out;

import com.back.product.domain.Product;
import com.back.product.domain.ProductOptionValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductOptionValuesRepository extends JpaRepository<ProductOptionValues, Long> {
    @Modifying
    @Query("update ProductOptionValues pov set pov.deletedAt = now() where pov.product in :products")
    void deleteAllByProductIn(List<Product> products);

    List<ProductOptionValues> findAllByProductIn(List<Product> products);
}
