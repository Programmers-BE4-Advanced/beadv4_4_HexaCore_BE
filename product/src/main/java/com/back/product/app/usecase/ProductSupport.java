package com.back.product.app.usecase;

import com.back.product.adapter.out.BrandRepository;
import com.back.product.adapter.out.CategoryRepository;
import com.back.product.adapter.out.OptionValueRepository;
import com.back.product.adapter.out.ProductInfoRepository;
import com.back.product.domain.Brand;
import com.back.product.domain.Category;
import com.back.product.domain.OptionValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductSupport {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductInfoRepository productInfoRepository;
    private final OptionValueRepository optionValueRepository;

    @Transactional(readOnly = true)
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean existsBrandByName(String name) {
        return brandRepository.existsBrandByNameIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean existsCategoryByName(String name) {
        return categoryRepository.existsCategoryByNameIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Optional<Brand> findBrandById(Long brandId) {
        return brandRepository.findById(brandId);
    }

    @Transactional(readOnly = true)
    public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Transactional(readOnly = true)
    public boolean existsProductInfoByBrandAndCode(Brand brand, String code) {
        return productInfoRepository.existsProductInfoByBrandAndProductCodeIgnoreCase(brand, code);
    }

    @Transactional(readOnly = true)
    public List<OptionValue> getAllOptionValues(List<Long> optionValueIds) {
        return optionValueRepository.findAllById(optionValueIds);
    }
}
