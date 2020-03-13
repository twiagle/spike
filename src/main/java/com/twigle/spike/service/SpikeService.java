package com.twigle.spike.service;

import com.twigle.spike.model.Orders;
import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpikeService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    @Transactional
    public Orders spike(SpikeUser spikeUser, GoodsVo goodsVo) {
        goodsService.reduceStock(goodsVo);
        return orderService.createOrder(spikeUser, goodsVo);
    }

}
