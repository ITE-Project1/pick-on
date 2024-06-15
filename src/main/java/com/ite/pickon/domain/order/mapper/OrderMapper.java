package com.ite.pickon.domain.order.mapper;

import com.ite.pickon.domain.order.dto.GetOrderRes;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

    GetOrderRes selectOrderById(@Param("orderId") String orderId);
}
