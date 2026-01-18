package com.back.notification.domain;

import com.back.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "notification_products")
public class NotificationProduct extends BaseTimeEntity {
    @Id
    @Column(name = "product_id")
    private Long id; // product 모듈의 ID 그대로 사용

    @Column(name = "product_option", nullable = false, length = 100)
    private String productOption;

    // Product_info 테이블
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "product_number", nullable = false)
    private String productNumber;

    @Column(name = "image")
    private String thumbnailImage;

    @Column(name = "release_price")
    private BigDecimal originalPrice;

    // Brand 테이블
    @Column(name = "brand_name", nullable = false)
    private String brandName;
    //Category 테이블
    @Column(name = "category_name", nullable = false)
    private String categoryName;
}
