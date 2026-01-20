package com.back.product.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductDto(
        ProductInfoDto productInfo,
        Long inventory,
        List<ProductOptionDto> options,
        List<String> imageUrls
) {
}

