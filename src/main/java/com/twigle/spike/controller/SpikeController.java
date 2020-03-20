package com.twigle.spike.controller;

import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.rabbitmq.MQSender;
import com.twigle.spike.rabbitmq.SpikeMessage;
import com.twigle.spike.redis.OrderPrefix;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.redis.SpikePrefix;
import com.twigle.spike.result.CodeMsg;
import com.twigle.spike.result.Result;
import com.twigle.spike.service.GoodsService;
import com.twigle.spike.service.OrderService;
import com.twigle.spike.service.SpikeService;
import com.twigle.spike.service.SpikeUserService;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/spike")
public class SpikeController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    SpikeService spikeService;
    @Autowired
    OrderService orderService;
    @Autowired
    SpikeUserService spikeUserService;
    @Autowired
    MQSender mqSender;
    @Autowired
    RedisService redisService;

    @RequestMapping("/do_spike")
    @ResponseBody
    Result<Integer> spike(SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
        if (spikeUser == null) return Result.error(CodeMsg.SESSION_ERROR);

        SpikeMessage spikeMessage = new SpikeMessage(spikeUser.getId(), goodsId);
        mqSender.sendSpikeMessage(spikeMessage);
        return Result.success(0);//排队中
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> spikeResult(SpikeUser user, @RequestParam("goodsId")long goodsId) {
        if(user == null) return Result.error(CodeMsg.SESSION_ERROR);

        long result = spikeService.getSpikeResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value="/reset", method= RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            //redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            //localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderPrefix.getSpikeOrderByUidGid);
        redisService.delete(SpikePrefix.isGoodsSoldOut);
        spikeService.reset(goodsList);
        return Result.success(true);
    }
}
