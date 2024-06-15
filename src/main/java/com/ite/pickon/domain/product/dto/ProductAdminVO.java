package com.ite.pickon.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@AllArgsConstructor
public class ProductAdminVO {
    private String id;
    private String name;
    private int price;
    private String imageUrl;
    private Date createdAt;
    private int quantity;

}
