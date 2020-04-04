package com.twigle.spike.controller;

import com.twigle.spike.domain.SpikeUser;
import com.twigle.spike.rabbitmq.MQSender;
import com.twigle.spike.rabbitmq.SpikeMessage;
import com.twigle.spike.redis.GoodsPrefix;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spike")
public class SpikeController implements InitializingBean {//初始化bean的方式，实现InitializingBean接口，实现afterPropertiesSet方法，spring将在bean的属性初始化后都会执行该方法
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

    private Map<Long, Boolean> soldOutFlag = new HashMap<>();//only for this Controller

    @RequestMapping("/do_spike")
    @ResponseBody
    Result<Integer> spike(SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
        if (spikeUser == null) return Result.error(CodeMsg.SESSION_ERROR);

        //redis 预减库存,这里有个问题：没考虑取消订单，所以不存在redis增加操作，减为负一定卖光。这里不控制并发，数据库保证
        if(soldOutFlag.get(goodsId)) return Result.error(CodeMsg.MIAO_SHA_OVER);
        long stock = redisService.decr(GoodsPrefix.getSpikeGoodsStock, "" + goodsId);
        if(stock<0){
            soldOutFlag.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

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
            goods.setStockCount(10);//spike count = 10
            redisService.set(GoodsPrefix.getSpikeGoodsStock, ""+goods.getId(), 10);
            soldOutFlag.put(goods.getId(), false);
        }
        redisService.delete(SpikePrefix.isGoodsSoldOut);
        redisService.delete(OrderPrefix.getSpikeOrderByUidGid);
        spikeService.reset(goodsList);
        return Result.success(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsPrefix.getSpikeGoodsStock, ""+goods.getId(), goods.getStockCount());
            soldOutFlag.put(goods.getId(), false);
        }
    }
}
