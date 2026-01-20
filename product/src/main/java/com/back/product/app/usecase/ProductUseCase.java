package com.back.product.app.usecase;

import com.back.product.adapter.out.ProductImageRepository;
import com.back.product.adapter.out.ProductOptionValuesRepository;
import com.back.product.adapter.out.ProductRepository;
import com.back.product.domain.*;
import com.back.product.dto.ProductDto;
import com.back.product.dto.request.ProductVariantCreateRequestDto;
import com.back.product.mapper.ProductImageMapper;
import com.back.product.mapper.ProductInfoMapper;
import com.back.product.mapper.ProductMapper;
import com.back.product.mapper.ProductOptionValuesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductUseCase {
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductOptionValuesMapper productOptionValuesMapper;
    private final ProductRepository productRepository;
    private final ProductOptionValuesRepository productOptionValuesRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public List<ProductDto> createProducts(
            ProductInfo productInfo,
            List<ProductVariantCreateRequestDto> variants,
            Map<Long, OptionValue> optionValueMap
    ) {
        List<Product> createdProducts = new ArrayList<>();
        List<ProductOptionValues> createdProductOptionValues = new ArrayList<>();
        List<ProductImage> createdImages = new ArrayList<>();

        variants.forEach(variant -> {
            // 1. Create Product
            Product newProduct = productMapper.toEntity(productInfo, variant);
            createdProducts.add(newProduct);

            // 2. Create ProductOptionValues (상품과 옵션 간의 관계 설정)
            List<ProductOptionValues> newProductOptionValues = variant.optionValueIds().stream().map(optionValueId -> {
                OptionValue optionValue = optionValueMap.get(optionValueId);
                return productOptionValuesMapper.toEntity(newProduct, optionValue);
            }).toList();
            createdProductOptionValues.addAll(newProductOptionValues);

            // 3. Create ProductImage
            List<ProductImage> newProductImages = variant.imageUrls().stream()
                    .map(imageUrl ->  productImageMapper.toEntity(newProduct, imageUrl)).toList();
            createdImages.addAll(newProductImages);
        });

        List<Product> newProducts = productRepository.saveAll(createdProducts);
        Map<Product, List<ProductOptionValues>> newProductOptionValuesAsMap = productOptionValuesRepository.saveAll(createdProductOptionValues)
                .stream().collect(Collectors.groupingBy(ProductOptionValues::getProduct));
        Map<Product, List<ProductImage>> newProductImagesAsMap = productImageRepository.saveAll(createdImages)
                .stream().collect(Collectors.groupingBy(ProductImage::getProduct));

        return newProducts.stream().map(product -> {
            List<ProductOptionValues> productOptionValues = newProductOptionValuesAsMap.get(product);
            List<ProductImage> productImages = newProductImagesAsMap.get(product);
            return productMapper.toDto(product, productOptionValues, productImages);
        }).toList();
    }
}
