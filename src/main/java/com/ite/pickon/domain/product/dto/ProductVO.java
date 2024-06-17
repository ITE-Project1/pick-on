package com.ite.pickon.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;
import java.sql.Date;
@Getter
@ToString
@AllArgsConstructor
public class ProductVO {
    private String productId; //pop_uuid 형식으로.
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private static String initPid() {
        String productId = "pop_" + UUID.randomUUID().toString();
        System.out.println(productId);
        return productId;
    }

    public static ProductVO of(ProductRequest productRequest) {
        return new ProductVO(initPid(),
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getImageUrl());

    }
}
