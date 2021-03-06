package com.twigle.spike.vo;

import com.twigle.spike.domain.Orders;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailVo {
    private GoodsVo goodsVo;
    private Orders order;
}
