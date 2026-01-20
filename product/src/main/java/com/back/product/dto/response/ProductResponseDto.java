package com.back.product.dto.response;

import com.back.product.dto.ProductDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductResponseDto(
        List<ProductDto> products
) {
}
