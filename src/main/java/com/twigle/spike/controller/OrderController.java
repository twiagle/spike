package com.twigle.spike.controller;

import com.twigle.spike.model.Orders;
import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.result.CodeMsg;
import com.twigle.spike.result.Result;
import com.twigle.spike.service.GoodsService;
import com.twigle.spike.service.OrderService;
import com.twigle.spike.vo.GoodsVo;
import com.twigle.spike.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail/{orderId}")
    @ResponseBody
    public Result<OrderDetailVo> detail(SpikeUser spikeUser, @PathVariable("orderId") long orderId) {
        if(spikeUser == null) return Result.error(CodeMsg.SESSION_ERROR);

        Orders order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = OrderDetailVo.builder().goodsVo(goodsVo).order(order).build();

        return Result.success(orderDetailVo);
    }
}

