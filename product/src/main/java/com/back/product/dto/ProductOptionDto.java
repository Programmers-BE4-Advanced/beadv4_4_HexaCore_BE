package com.back.product.dto;

import lombok.Builder;

@Builder
public record ProductOptionDto(
        String groupName,
        String value
) {
}
