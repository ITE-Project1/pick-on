package com.ite.pickon.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ite.pickon.util.KSTDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderResponse {
    private String orderId;
    @JsonSerialize(using = KSTDateSerializer.class)
    private Date orderDate;
    @JsonSerialize(using = KSTDateSerializer.class)
    private Date pickupDate;
    private String fromStore;
    private String toStore;
    private String pickupStatus;
    private String productImg;
    private String productName;
    private String productId;
    private int quantity;
    private int totalPrice;
    private String userPhoneNumber;
}
