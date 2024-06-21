package com.ite.pickon.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//상품목록 조회용 VO
@Getter
@AllArgsConstructor
@ToString
public class ProductListVO {
    private String productId;
    private String name;
    private int price;
    private String imageUrl;
    private String brandName;
}
