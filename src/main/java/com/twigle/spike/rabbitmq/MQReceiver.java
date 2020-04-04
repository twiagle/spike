package com.twigle.spike.rabbitmq;

import com.twigle.spike.domain.SpikeOrders;
import com.twigle.spike.domain.SpikeUser;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.service.GoodsService;
import com.twigle.spike.service.OrderService;
import com.twigle.spike.service.SpikeService;
import com.twigle.spike.service.SpikeUserService;
import com.twigle.spike.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    SpikeService spikeService;
    @Autowired
    SpikeUserService spikeUserService;

    @RabbitListener(queues = MQConfig.SPIKE_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);

        SpikeMessage spikeMessage = RedisService.stringToBean(message, SpikeMessage.class);
        long userId = spikeMessage.getUserId();
        long goodsId = spikeMessage.getGoodsId();
        SpikeUser user = spikeUserService.getById(userId);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        if(goods.getStockCount()<=0) {
            spikeService.setGoodsSoldOut(goodsId);
            return;
        }
        SpikeOrders spikeOrders = orderService.getSpikeOrderByUserIDGoodsID(userId, goodsId);
        if(spikeOrders != null) return;

        spikeService.spike(user, goods);
        log.info("spike over");
    }
}
