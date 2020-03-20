package com.twigle.spike.controller;

import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.redis.GoodsPrefix;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.result.Result;
import com.twigle.spike.service.GoodsService;
import com.twigle.spike.vo.GoodsDetailVo;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    //    @Autowired
//    SpikeService spikeService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * redis store html
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/to_list")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {

        String html = redisService.get(GoodsPrefix.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) return html;

        List<GoodsVo> goodsList = goodsService.listGoodsVo();

        model.addAttribute("goodsList", goodsList);
        IContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);

        if (!StringUtils.isEmpty(html)) redisService.set(GoodsPrefix.getGoodsList, "", html);
        return html;
    }

    /**
     * browser store static htm, ajax get data
     *
     * @param model
     * @param spikeUser
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, SpikeUser spikeUser, @PathVariable("goodsId") long goodsId) {

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int spikeStatus = 0;
        int remainSeconds = 0;

        if (now < startAt) {
            spikeStatus = 0;// not begin
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            spikeStatus = 2;// end
            remainSeconds = -1;
        } else {
            spikeStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = GoodsDetailVo.builder().goodsVo(goodsVo).spikeUser(spikeUser).spikeStatus(spikeStatus).remainSeconds(remainSeconds).build();

        return Result.success(goodsDetailVo);
    }

}
