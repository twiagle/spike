package com.twigle.spike.vo;

import com.twigle.spike.model.Goods;
import lombok.Data;

import java.util.Date;

/**
 *      goods  +  last 4 elements of spikeGoods
 */
@Data
public class GoodsVo extends Goods {
    private Double spikePrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
