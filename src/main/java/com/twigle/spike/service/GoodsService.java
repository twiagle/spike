package com.twigle.spike.service;

import com.twigle.spike.dao.GoodsDao;
import com.twigle.spike.model.SpikeGoods;
import com.twigle.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goodsVo) {
        SpikeGoods g = new SpikeGoods();
        g.setGoodsId(goodsVo.getId());
        goodsDao.reduceStock(g);
    }

}
