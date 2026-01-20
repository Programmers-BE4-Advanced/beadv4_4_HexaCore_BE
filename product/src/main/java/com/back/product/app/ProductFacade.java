package com.back.product.app;

import com.back.product.app.usecase.*;
import com.back.product.domain.*;
import com.back.product.dto.CategoryDto;
import com.back.product.dto.ProductDto;
import com.back.product.dto.request.BrandCreateRequestDto;
import com.back.product.dto.BrandDto;
import com.back.product.dto.request.CategoryCreateRequestDto;
import com.back.product.dto.request.ProductCreateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final BrandUseCase brandUseCase;
    private final CategoryUseCase categoryUseCase;
    private final OptionUseCase optionUseCase;
    private final ProductInfoUseCase productInfoUseCase;
    private final ProductUseCase productUseCase;

    @Transactional(readOnly = true)
    public List<BrandDto> getBrands() {
        return brandUseCase.getBrands();
    }

    @Transactional
    public BrandDto createBrand(@Valid BrandCreateRequestDto request) {
        return brandUseCase.createBrand(request);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {
        return categoryUseCase.getCategories();
    }

    @Transactional
    public CategoryDto createCategory(@Valid CategoryCreateRequestDto request) {
        return categoryUseCase.createCategory(request);
    }

    @Transactional
    public List<ProductDto> createProduct(@Valid ProductCreateRequestDto request) {
        Brand brand = brandUseCase.findBrandExists(request.productInfo().brandId());

        Category category = categoryUseCase.findCategoryExists(request.productInfo().categoryId());

        List<Long> optionValueIds = request.variants().stream()
                .flatMap(variant -> variant.optionValueIds().stream()).distinct().toList();
        Map<Long, OptionValue> optionValueMap = optionUseCase.findOptionValuesAsMap(optionValueIds);

        ProductInfo productInfo = productInfoUseCase.createProductInfo(brand, category, request.productInfo());

        return productUseCase.createProducts(productInfo, request.variants(), optionValueMap);
    }
}
