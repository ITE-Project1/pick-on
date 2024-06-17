package com.ite.pickon.domain.product.dto;
//상품 상세 -> ProductVO 만들기 위해 쓰임
//사용자 직접 입력 사용 X
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
    private int storeId;
    private int quantity;
}
