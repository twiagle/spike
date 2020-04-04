package com.twigle.spike.vo;

import com.twigle.spike.domain.Goods;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *      goods  +  last 4 elements of spikeGoods
 */
@Data
public class GoodsVo extends Goods {
    private Double spikePrice;
    private Integer stockCount;//spikeCount
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
