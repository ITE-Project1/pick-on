package com.ite.pickon.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Builder
@Getter
@ToString
public class GetOrderRes {
    private String orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pickupDate;
    private String fromStore;
    private String pickupStatus;
    private String productName;
    private String productId;
    private int quantity;
    private int totalPrice;
}
