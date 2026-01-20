package com.back.product.app.usecase;

import com.back.product.adapter.out.ProductImageRepository;
import com.back.product.adapter.out.ProductOptionValuesRepository;
import com.back.product.adapter.out.ProductRepository;
import com.back.product.domain.*;
import com.back.product.dto.ProductDto;
import com.back.product.dto.request.ProductVariantCreateRequestDto;
import com.back.product.mapper.ProductImageMapper;
import com.back.product.mapper.ProductMapper;
import com.back.product.mapper.ProductOptionValuesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
            Product newProduct = createProduct(productInfo, variant.inventory());
            createdProducts.add(newProduct);

            List<ProductOptionValues> newProductOptionValues = createProductOptionValues(newProduct, variant.optionValueIds(), optionValueMap);
            createdProductOptionValues.addAll(newProductOptionValues);

            List<ProductImage> newProductImages = createProductImages(newProduct, variant.imageUrls());
            createdImages.addAll(newProductImages);
        });

        List<Product> newProducts = productRepository.saveAll(createdProducts);
        List<ProductOptionValues> newProductOptionValues = productOptionValuesRepository.saveAll(createdProductOptionValues);
        List<ProductImage> newProductImages = productImageRepository.saveAll(createdImages);

        return convertToDto(newProducts, newProductOptionValues, newProductImages);
    }
    
    private Product createProduct(ProductInfo productInfo, Long inventory) {
        return productMapper.toEntity(productInfo, inventory);
    }

    private List<ProductOptionValues> createProductOptionValues(Product product, List<Long> optionValueIds, Map<Long, OptionValue> optionValueMap) {
        return optionValueIds.stream().map(optionValueId -> {
            OptionValue optionValue = optionValueMap.get(optionValueId);
            return productOptionValuesMapper.toEntity(product, optionValue);
        }).toList();
    }

    private List<ProductImage> createProductImages(Product product, List<String> imageUrls) {
        return imageUrls.stream().map(imageUrl ->
                productImageMapper.toEntity(product, imageUrl)
        ).toList();
    }

    private List<ProductDto> convertToDto(
            List<Product> products,
            List<ProductOptionValues> productOptionValues,
            List<ProductImage> productImages
    ) {
        Map<Product, List<ProductOptionValues>> productOptionValuesAsMap = productOptionValues
                .stream().collect(Collectors.groupingBy(ProductOptionValues::getProduct));
        Map<Product, List<ProductImage>> productImagesAsMap = productImages.stream()
                .collect(Collectors.groupingBy(ProductImage::getProduct));

        return products.stream().map(product ->
            productMapper.toDto(
                product,
                productOptionValuesAsMap.get(product),
                productImagesAsMap.get(product)
            )
        ).toList();
    }
}
