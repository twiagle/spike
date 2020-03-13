package com.twigle.spike.model;

import lombok.Data;

@Data
public class SpikeOrders {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
