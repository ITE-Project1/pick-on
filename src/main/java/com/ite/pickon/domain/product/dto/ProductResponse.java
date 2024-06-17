package com.ite.pickon.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String productId;
    private String name;
    private String description;
    private int price;
    private String imageUrl;
}