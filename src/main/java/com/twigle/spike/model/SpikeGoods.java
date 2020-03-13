package com.twigle.spike.model;

import lombok.Data;

import java.util.Date;

@Data
public class SpikeGoods {
    private Long id;//auto increment
    private Long goodsId;//Goods.id
    private Double spikePrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
