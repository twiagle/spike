package com.twigle.spike.vo;

import com.twigle.spike.domain.SpikeUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoodsDetailVo {
    private int spikeStatus;
    private int remainSeconds;
    private GoodsVo goodsVo;
    private SpikeUser spikeUser;
}
