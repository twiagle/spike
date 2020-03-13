package com.twigle.spike.controller;

import com.twigle.spike.model.Orders;
import com.twigle.spike.model.SpikeOrders;
import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.result.CodeMsg;
import com.twigle.spike.service.GoodsService;
import com.twigle.spike.service.OrderService;
import com.twigle.spike.service.SpikeService;
import com.twigle.spike.service.SpikeUserService;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @RequestMapping("/do_spike")
    String spike(Model model, SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("spikeUser", spikeUser);
        if (spikeUser == null) return "login";
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);

        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "order_fail";
        }

        SpikeOrders spikeOrder = orderService.getSpikeOrderByUserIDGoodsID(spikeUser.getId(), goodsId);
        if (null != spikeOrder) {
            model.addAttribute(("errmsg"), CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "order_fail";
        }

        Orders order = spikeService.spike(spikeUser, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goodsVo", goodsVo);
        return "order_detail";
    }
}
