package com.ite.pickon.domain.product.dto;
//상품 등록, product id 생성된 VO
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;
import java.sql.Date;
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private String productId; //사용자 입력
    private String name;
    private String description;
    private int price;
    private String imageUrl;
//    private static String initPid() {
//        String productId = "pop_" + UUID.randomUUID().toString();
//        System.out.println(productId);
//        return productId;
//    }

//    public static ProductVO of(ProductRequest productRequest) {
//        return new ProductVO(initPid(),
//                productRequest.getName(),
//                productRequest.getDescription(),
//                productRequest.getPrice(),
//                productRequest.getImageUrl());
//
//    }
}
