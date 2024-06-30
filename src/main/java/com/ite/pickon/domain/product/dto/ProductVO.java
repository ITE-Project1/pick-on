package com.ite.pickon.domain.product.dto;
//상품 등록, product id 생성된 VO
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;
import java.sql.Date;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVO {
    private String productId;
    private int brandId;
    private String name;
    private String description;
    private int price;
    private String imageUrl;

}
