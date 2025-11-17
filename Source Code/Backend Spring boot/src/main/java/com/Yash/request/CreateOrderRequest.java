package com.Yash.request;

import com.Yash.domain.OrderType;

import com.Yash.model.Coin;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
