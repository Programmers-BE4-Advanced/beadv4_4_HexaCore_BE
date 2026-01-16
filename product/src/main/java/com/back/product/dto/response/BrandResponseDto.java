package com.back.product.dto.response;

import lombok.Builder;

@Builder
public record BrandResponseDto(
        String name,
        String logoUrl
) {
}
