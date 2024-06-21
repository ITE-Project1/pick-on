package com.ite.pickon.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pickupDate;
    private String pickupStatus;
    private String productImg;
    private int totalPrice;
    private String brandName;
    private String storeName;
}
