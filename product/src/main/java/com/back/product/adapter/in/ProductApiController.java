package com.back.product.adapter.in;

import com.back.common.response.CommonResponse;
import com.back.product.dto.request.ProductCreateRequestDto;
import com.back.product.dto.request.ProductUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product", description = "상품 관련 API")
public interface ProductApiController {
    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "상품 생성 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    CommonResponse<?> createProduct(ProductCreateRequestDto request);

    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    CommonResponse<?> updateProduct(Long productInfoId, ProductUpdateRequestDto request);
}
