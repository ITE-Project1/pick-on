package com.ite.pickon.domain.product.dto;
//상품 상세
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
//상품등록
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private int brandId;
}