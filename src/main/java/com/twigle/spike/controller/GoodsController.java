package com.twigle.spike.controller;

import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.service.GoodsService;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;


    @RequestMapping("/to_list")
    public String list(Model model) {

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, SpikeUser spikeUser, @PathVariable("goodsId") long goodsId) {
        if (spikeUser == null) return "login";
        model.addAttribute("spikeUser", spikeUser);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goodsVo", goodsVo);

        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int spikeStatus = 0;
        long remainSeconds = 0;

        if (now < startAt) {
            spikeStatus = 0;// not begin
            remainSeconds = (now - startAt) / 1000;
        } else if (now > endAt) {
            spikeStatus = 2;// end
            remainSeconds = -1;
        } else {
            spikeStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("spikeStatus", spikeStatus);
        model.addAttribute("remainSeconds", remainSeconds);


        return "goods_detail";
    }

}
