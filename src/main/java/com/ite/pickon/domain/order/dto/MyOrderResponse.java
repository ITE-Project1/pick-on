package com.ite.pickon.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ite.pickon.util.KSTDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderResponse {
    private String orderId;
    @JsonSerialize(using = KSTDateSerializer.class)
    private Date orderDate;
    @JsonSerialize(using = KSTDateSerializer.class)
    private Date pickupDate;
    private String pickupStatus;
    private String productImg;
    private String productName;
    private int totalPrice;
    private String brandName;
    private String storeName;
}
